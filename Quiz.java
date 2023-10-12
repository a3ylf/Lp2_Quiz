import java.io.*;
import java.util.Random;


public class Quiz {


    public static void quiz(DataInputStream in, DataOutputStream out) throws IOException {

        Questions[] questionArray = new Questions[50];
        //exemplo
        questionArray[0] = new Questions("easy", "B", """
                Você é homem?
                A - não
                B - sim
                C - talvez""");



        String userInput;
        out.writeUTF("""
                Porfavor escolha a dificuldade:
                1-facil (6 perguntas fáceis, 3 perguntas médias e 1 difícil)
                2-média (3 perguntas fáceis, 5 perguntas médias e 2 difíceis)
                3-dificil (2 perguntas fáceis, 4 perguntas médias e 4 difíceis)
                4-muito dificil (1 pergunta fácil, 3 perguntas médias e 6 difíceis)
                """);

         do {
            int pontuacaofinal;
            userInput = in.readUTF();
            switch (userInput) {
                case "1" -> {
                  pontuacaofinal=easyquiz(in,out);
                  if(pontuacaofinal>=8&&pontuacaofinal<=10){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 1000 reais");
                    }
                    else if(pontuacaofinal>=5&&pontuacaofinal<=7){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 500 reais");
                    }
                    else if(pontuacaofinal>=3&&pontuacaofinal<=4){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 100 reais");
                    }
                    else if(pontuacaofinal>=1&&pontuacaofinal<=2){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 50 reais");
                    }
                    else{
                        out.writeUTF("Você não ganhou nada");
                  }
                  out.writeUTF("break");
                  break;
                }

                case "2" -> {
                   pontuacaofinal= mediumquiz(in,out);
                   if(pontuacaofinal>=8&&pontuacaofinal<=10){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 1000 reais");
                    }
                    else if(pontuacaofinal>=5&&pontuacaofinal<=7){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 500 reais");
                    }
                    else if(pontuacaofinal>=3&&pontuacaofinal<=4){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 100 reais");
                    }
                    else if(pontuacaofinal>=1&&pontuacaofinal<=2){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 50 reais");
                    }
                    else{
                        out.writeUTF("Você não ganhou nada");
                  }
                  out.writeUTF("break");
                  break;
                }

                case "3" -> {
                    pontuacaofinal=hardquiz(in,out);
                    if(pontuacaofinal>=8&&pontuacaofinal<=10){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 1000 reais");
                    }
                    else if(pontuacaofinal>=5&&pontuacaofinal<=7){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 500 reais");
                    }
                    else if(pontuacaofinal>=3&&pontuacaofinal<=4){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 100 reais");
                    }
                    else if(pontuacaofinal>=1&&pontuacaofinal<=2){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 50 reais");
                    }
                    else{
                        out.writeUTF("Você não ganhou nada");
                  }
                  out.writeUTF("break");
                  break;
                }

                case "4" -> {
                   pontuacaofinal= veryhardquiz(in, out);
                    if(pontuacaofinal>=8&&pontuacaofinal<=10){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 1000 reais");
                    }
                    else if(pontuacaofinal>=5&&pontuacaofinal<=7){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 500 reais");
                    }
                    else if(pontuacaofinal>=3&&pontuacaofinal<=4){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 100 reais");
                    }
                    else if(pontuacaofinal>=1&&pontuacaofinal<=2){
                        out.writeUTF("Parabéns, você acertou "+pontuacaofinal+" questões e ganhou um prêmio de 50 reais");
                    }
                    else{
                        out.writeUTF("Você não ganhou nada");
                  }
                  out.writeUTF("break");
                  break;
                }


                default -> {out.writeUTF("Escolha inválida");out.writeUTF("break");break;}
            }
        } while (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3") && userInput.equals("4"));
    }




    private static int easyquiz(DataInputStream in, DataOutputStream out) throws IOException {
        int pontuacao = 0;
        Random random = new Random();
        String userAnswer;
        int randomIndex;
        for (int i = 0; i < 6; i++) {
            randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();
        }
        for(int i=0;i<3;i++){
           randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();
        }
        randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();
        return pontuacao;

    }
    private static int mediumquiz(DataInputStream in, DataOutputStream out) throws IOException {
        int pontuacao = 0;
        Random random = new Random();
        String userAnswer;
        int randomIndex;
        for (int i = 0; i < 3; i++) {
            randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush(); // Limpa o buffer de saída
        }
        
        
        for(int i=0;i<5;i++){
            randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush(); // Limpa o buffer de saída
        }
        for(int i=0;i<2;i++){
            randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush(); // Limpa o buffer de saída}
        }
        return pontuacao;}
    private static int hardquiz(DataInputStream in, DataOutputStream out) throws IOException {
        int pontuacao = 0;
        Random random = new Random();
        String userAnswer;
        int randomIndex;

        for (int i = 0; i < 2; i++) {
            randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();
        }
        for(int i=0;i<4;i++){
            randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();
        }
        for(int i=0;i<4;i++){
        randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();}
        return pontuacao;
    }
    private static int veryhardquiz(DataInputStream in, DataOutputStream out) throws IOException {
        int pontuacao = 0;
        Random random = new Random();
        String userAnswer;
        int randomIndex;
        
        for(int i=0;i<4;i++){
            randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();
        }
        for(int i=0;i<4;i++){
        randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();}
        
        randomIndex = random.nextInt(10);
            out.writeUTF(Questions.easyQuestions[randomIndex].getQuestion());
            out.flush(); // Limpa o buffer de saída
            userAnswer = in.readUTF();
            while (userAnswer.equals("")) {
                userAnswer = in.readUTF();
            }
            if (Questions.easyQuestions[randomIndex].getAnswer().equals(userAnswer)) {
                pontuacao++;
                out.writeUTF("A Resposta " + userAnswer + " está correta");
            } else {
                out.writeUTF("A Resposta " + userAnswer + " está errada");
            }
            out.flush();return pontuacao;}



}
