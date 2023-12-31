import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class Quiz {

    public static void quiz(DataInputStream in, DataOutputStream out) throws
            IOException, InterruptedException {

        Semaphore roomadd = protocolServer.getRoomadd();
        List<multiplayerRoom> salas = protocolServer.getSalas();
        String userInput;
        mode.write(out);
        out.writeUTF("""
                Você deseja jogar em qual desses modos:
                1-Normal
                2-Multijogador
                3-Competitivo
                4-voltar
                """);

        do {
            userInput = in.readUTF();
            switch (userInput) {
                case "1" -> quiznormal(in, out);
                case "2" -> quizmultiplayer(in, out, roomadd, salas);
                case "3" -> quizInfinito(in,out);
                case "4" -> protocolServer.menu(in, out);
                default -> {
                    mode.write(out);
                    out.writeUTF("Escolha inválida");
                }
            }
        } while (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3") && !userInput.equals("4"));
    }

    private static void quiznormal(DataInputStream in, DataOutputStream out) throws IOException, InterruptedException {
        String userInput;
        mode.write(out);
        out.writeUTF("""
                Porfavor escolha a dificuldade:
                1-facil (6 perguntas fáceis, 3 perguntas médias e 1 dificil)
                2-média (3 perguntas fáceis, 4 perguntas médias e 3 dificeis)
                3-dificil (2 perguntas fáceis, 3 perguntas médias e 5 dificeis)
                4-muito dificil (1 pergunta fácil, 2 perguntas médias e 7 dificeis)
                """);

        do {
            userInput = in.readUTF();
            switch (userInput) {
                case "1" -> quiz(6, 3, 1, in, out);
                case "2" -> quiz(3, 4, 3, in, out);
                case "3" -> quiz(2, 3, 5, in, out);
                case "4" -> quiz(1, 2, 7, in, out);
                default -> {
                    mode.write(out);
                    out.writeUTF("Escolha inválida");
                }
            }
        } while (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3") && !userInput.equals("4"));

    }

    private static void quizmultiplayer(DataInputStream in, DataOutputStream out, Semaphore roomadd, List<multiplayerRoom> salas) throws IOException, InterruptedException {
        mode.write(out);
        out.writeUTF("""
                1-Criar sala
                2-Entrar em sala
                3-Voltar
                """);
        String userInput;
        do {
            userInput = in.readUTF();
            switch (userInput) {
                case "1" -> CreateRoom(in, out);
                case "2" -> EnterRoom(in, out);
                case "3" -> Quiz.quiz(in, out);
                default -> {
                    mode.write(out);
                    out.writeUTF("Escolha inválida");
                }
            }
        } while (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3"));
    }


    private static void CreateRoom(DataInputStream in, DataOutputStream out) throws IOException, InterruptedException {

        String userInput;
        mode.write(out);
        Semaphore roomadd = protocolServer.getRoomadd();
        List<multiplayerRoom> salas = protocolServer.getSalas();
        out.writeUTF("""
                Diga a quantidade máxima de pessoas na sala (Minimo 2, Máximo 5)
                """);
        userInput = in.readUTF();
        while (!userInput.equals("2") && !userInput.equals("3") && !userInput.equals("4") && !userInput.equals("5")) {
            mode.write(out);
            out.writeUTF("Escolha inválida");
            userInput = in.readUTF();
        }
        int Playermax = Integer.parseInt(userInput);
        mode.write(out);
        out.writeUTF("""
                Porfavor escolha a dificuldade:
                1-facil (6 perguntas fáceis, 3 perguntas médias e 1 dificil)
                2-média (3 perguntas fáceis, 4 perguntas médias e 3 dificeis)
                3-dificil (2 perguntas fáceis, 3 perguntas médias e 5 dificeis)
                4-muito dificil (1 pergunta fácil, 2 perguntas médias e 7 dificeis)
                """);
        do {
            userInput = in.readUTF();
            switch (userInput) {
                case "1" -> {
                    roomadd.acquire();
                    int index = salas.size();
                    salas.add(new multiplayerRoom(Playermax, "easy"));
                    salas.get(index).setRoomindex(index);
                    salas.get(index).setDatastream(in, out);
                    roomadd.release();

                }
                case "2" -> {
                    roomadd.acquire();
                    int index = salas.size();
                    salas.add(new multiplayerRoom(Playermax, "medium"));
                    salas.get(index).setRoomindex(index);
                    salas.get(index).setDatastream(in, out);
                    roomadd.release();
                }
                case "3" -> {
                    roomadd.acquire();
                    int index = salas.size();
                    salas.add(new multiplayerRoom(Playermax, "hard"));
                    salas.get(index).setRoomindex(index);
                    salas.get(index).setDatastream(in, out);
                    roomadd.release();
                }
                case "4" -> {
                    roomadd.acquire();
                    int index = salas.size();
                    salas.add(new multiplayerRoom(Playermax, "veryhard"));
                    salas.get(index).setRoomindex(index);
                    salas.get(index).setDatastream(in, out);
                    roomadd.release();
                }
                case "back" -> {
                    Quiz.quiz(in, out);
                }

                default -> {
                    mode.write(out);
                    out.writeUTF("Escolha inválida");
                }
            }
        } while (!userInput.equals("1") && !userInput.equals("2") && !userInput.equals("3") && !userInput.equals("4") && !userInput.equals("back"));

        mode.read(out);
        out.writeUTF("Aguardando a entrada de outros jogadores, você está sozinho!");


    }

    private static void EnterRoom(DataInputStream in, DataOutputStream out) throws IOException, InterruptedException {
        String userInput;
        mode.read(out);
        Semaphore roomadd = protocolServer.getRoomadd();
        List<multiplayerRoom> salas = protocolServer.getSalas();
        boolean connected = false;
        int index = 0;
        out.writeUTF(" Salas disponiveis:");
        for (multiplayerRoom sala : salas) {
            mode.read(out);
            out.writeUTF("Sala " + sala.getRoomindex() + " Jogadores conectados: " + sala.getplayers());
        }
        mode.write(out);
        out.writeUTF("Digite o código da sala que você quer entrar :");
        userInput = in.readUTF();
        while (!userInput.equals("0") && !userInput.equals("1") && !userInput.equals("2")
                && !userInput.equals("3") && !userInput.equals("4") ) {
            mode.write(out);
            out.writeUTF("Escolha inválida");
            userInput = in.readUTF();
        }

        for (multiplayerRoom sala : salas) {
            if (sala.getRoomindex() == Integer.parseInt(userInput)) {
                index = Integer.parseInt(userInput);
                roomadd.acquire();
                sala.setDatastream(in, out);
                mode.read(out);
                out.writeUTF("Conexão bem sucedida!");
                roomadd.release();
                connected = true;
                break;
            }
        }
        if (!connected) {
            mode.read(out);
            out.writeUTF("Erro ao conectar, voltando ao menu");
            Thread.sleep(2000);
            protocolServer.menu(in, out);
        } else {
            for (DataOutputStream outs : salas.get(index).getOuts()) {
                mode.read(outs);
                outs.writeUTF("Jogador " + salas.get(index).getplayers() + " Conectado");
            }
            if (salas.get(index).getplayers() == salas.get(index).getPlayermax()) {
                multiquiz(salas.get(index).getDifficulty(), salas.get(index).getIns(), salas.get(index).getOuts(), index);
            }
        }
    }

    private static void multiquiz(String difficulty, DataInputStream[] ins, DataOutputStream[] outs, int index) throws IOException, InterruptedException {

        String fromuser;
        int[] easy;
        int[] medium;
        int[] hard;
        int easysize = 0;
        int mediumsize = 0;
        int hardsize = 0;
        if (difficulty.equals("easy")) {
            easysize = 6;
            mediumsize = 3;
            hardsize = 1;
        }
        if (difficulty.equals("medium")) {
            easysize = 3;
            mediumsize = 4;
            hardsize = 3;
        }
        if (difficulty.equals("hard")) {
            easysize = 2;
            mediumsize = 3;
            hardsize = 5;
        }
        if (difficulty.equals("veryhard")) {
            easysize = 1;
            mediumsize = 2;
            hardsize = 7;
        }
        int[] total = new int[ins.length];
        Thread[] restartThreads = new Thread[ins.length];
        for (DataOutputStream out : outs) {
            mode.read(out);
            out.writeUTF("_________________________\nA partida vai começar:");
        }
        Thread.sleep(3000);
        easy = genRandomArray(easysize);
        medium = genRandomArray(mediumsize);
        hard = genRandomArray(hardsize);
        try {
            for (int i = 0; i < easysize; i++) {
                for (int j = 0; j < outs.length; j++) {
                    mode.write(outs[j]);
                    outs[j].writeUTF(Questions.easyQuestions[easy[i]].question());
                    outs[j].flush();
                    fromuser = ins[j].readUTF();
                    while (fromuser.isEmpty()) {
                        mode.write(outs[j]);
                        outs[j].writeUTF("");
                        fromuser = ins[j].readUTF();
                    }
                    if (Questions.easyQuestions[easy[i]].answer().equalsIgnoreCase(fromuser)) {

                        total[j] = total[j] + 20;
                        mode.read(outs[j]);
                        outs[j].writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total[j]);
                    } else {
                        mode.read(outs[j]);
                        outs[j].writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total[j]);
                    }

                }
            }
            for (int i = 0; i < mediumsize; i++) {
                for (int j = 0; j < outs.length; j++) {
                    mode.write(outs[j]);
                    outs[j].writeUTF(Questions.mediumQuestions[medium[i]].question());
                    outs[j].flush();
                    fromuser = ins[j].readUTF();
                    while (fromuser.isEmpty()) {
                        mode.write(outs[j]);
                        outs[j].writeUTF("");
                        fromuser = ins[j].readUTF();
                    }
                    if (Questions.mediumQuestions[medium[i]].answer().equalsIgnoreCase(fromuser)) {

                        total[j] = total[j] + 50;
                        mode.read(outs[j]);
                        outs[j].writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total[j]);
                    } else {
                        mode.read(outs[j]);
                        outs[j].writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total[j]);
                    }

                }
            }
            for (int i = 0; i < hardsize; i++) {
                for (int j = 0; j < outs.length; j++) {
                    mode.write(outs[j]);
                    outs[j].writeUTF(Questions.hardQuestions[hard[i]].question());
                    outs[j].flush();
                    fromuser = ins[j].readUTF();
                    while (fromuser.isEmpty()) {
                        mode.write(outs[j]);
                        outs[j].writeUTF("");
                        fromuser = ins[j].readUTF();
                    }
                    if (Questions.hardQuestions[hard[i]].answer().equalsIgnoreCase(fromuser)) {

                        total[j] = total[j] + 100;
                        mode.read(outs[j]);
                        outs[j].writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total[j]);
                    } else {
                        mode.read(outs[j]);
                        outs[j].writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total[j]);
                    }

                }
            }
        } catch (Exception e){
            for (int i = 0; i < ins.length; i++) {
                int finalI = i;
                restartThreads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mode.read(outs[finalI]);
                            outs[finalI].writeUTF("Ocorreu um erro com algum jogador da sala, você voltará ao menu");
                            Thread.sleep(2000);
                            protocolServer.menu(ins[finalI], outs[finalI]);
                        } catch (IOException | InterruptedException ignored) {}
                    }
                });
                restartThreads[i].start();
                Semaphore roomadd = protocolServer.getRoomadd();
                List<multiplayerRoom> salas = protocolServer.getSalas();
                roomadd.acquire();
                salas.remove(index);
                roomadd.release();
            }

        }
        int pontosVencedor = 0;
        int vencedor = -1;
        for (int i = 0; i < total.length; i++) {
            if (total[i] > pontosVencedor) {
                pontosVencedor = total[i];
                vencedor = i;
            }
        }
        for (DataOutputStream out : outs) {
            mode.read(out);
            out.writeUTF("O vencedor foi o jogador  " + vencedor + " com " + pontosVencedor + " pontos!\n\n");
        }
        Thread.sleep(5000);
        for (DataOutputStream out : outs) {
            mode.read(out);
            out.writeUTF("Você será retornado ao menu");
        }
        Thread.sleep(4000);


        for (int i = 0; i < ins.length; i++) {
            int finalI = i;
            restartThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        protocolServer.menu(ins[finalI], outs[finalI]);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            restartThreads[i].start();
        }

        Semaphore roomadd = protocolServer.getRoomadd();
        List<multiplayerRoom> salas = protocolServer.getSalas();
        roomadd.acquire();
        salas.remove(index);
        roomadd.release();

    }

    private static int[] genRandomArray(int size) {

        Random random = new Random();
        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            int value;
            boolean contains;

            do {
                value = random.nextInt(10);
                contains = false;

                for (int j = 0; j < i; j++) {
                    if (array[j] == value) {
                        contains = true;
                        break;
                    }
                }
            } while (contains);

            array[i] = value;
        }
        return array;
    }


    private static void quiz(int easysize, int mediumsize, int hardsize, DataInputStream in, DataOutputStream out) throws IOException, InterruptedException {
        int[] easy = genRandomArray(easysize);
        int[] medium = genRandomArray(mediumsize);
        int[] hard = genRandomArray(hardsize);
        Thread.sleep(2000);
        mode.read(out);
        out.writeUTF("----------------------\nO Quiz vai começar");
        Thread.sleep(2000);
        int total = 0;
        String fromuser;
        for (int i = 0; i < easysize; i++) {
            mode.write(out);
            out.writeUTF(Questions.easyQuestions[easy[i]].question());
            out.flush();
            fromuser = in.readUTF();
            while (fromuser.isEmpty()) {
                mode.write(out);
                out.writeUTF("");
                fromuser = in.readUTF();
            }
            if (Questions.easyQuestions[easy[i]].answer().equalsIgnoreCase(fromuser)) {
                total = total + 20;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total);
            } else {
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total);
            }
            Thread.sleep(500);
        }
        for (int i = 0; i < mediumsize; i++) {
            mode.write(out);
            out.writeUTF(Questions.mediumQuestions[medium[i]].question());
            out.flush();
            fromuser = in.readUTF();
            while (fromuser.isEmpty()) {
                mode.write(out);
                out.writeUTF("");
                fromuser = in.readUTF();
            }
            if (Questions.mediumQuestions[medium[i]].answer().equalsIgnoreCase(fromuser)) {
                total = total + 50;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total);
            } else {
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total);
            }

            Thread.sleep(500);
        }
        for (int i = 0; i < hardsize; i++) {
            mode.write(out);
            out.writeUTF(Questions.hardQuestions[hard[i]].question());
            out.flush();
            fromuser = in.readUTF();
            while (fromuser.isEmpty()) {
                mode.write(out);
                out.writeUTF("");
                fromuser = in.readUTF();
            }
            if (Questions.hardQuestions[hard[i]].answer().equalsIgnoreCase(fromuser)) {
                total = total + 100;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total);
            } else {
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total);
            }
            Thread.sleep(500);
        }
        mode.read(out);
        out.writeUTF("Você conseguiu: " + total + " pontos");
        protocolServer.menu(in, out);
    }

    private static void quizInfinito(DataInputStream in, DataOutputStream out) throws IOException, InterruptedException {
        mode.read(out);
        out.writeUTF("A partida vai começar");
        Thread.sleep(3000);
        int[] easy = genRandomArray(Questions.easyQuestions.length);
        int[] medium = genRandomArray(Questions.mediumQuestions.length);
        int[] hard = genRandomArray(Questions.hardQuestions.length);
        int total = 0;
        int vidas = 3;
        String fromuser;
        for (int i = 0; i < easy.length; i++) {
            mode.write(out);
            out.writeUTF(Questions.easyQuestions[easy[i]].question());
            out.flush();
            fromuser = in.readUTF();
            while (fromuser.isEmpty()) {
                mode.write(out);
                out.writeUTF("");
                fromuser = in.readUTF();
            }
            if (Questions.easyQuestions[easy[i]].answer().equalsIgnoreCase(fromuser)) {
                total = total + 20;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total);
            } else {
                vidas--;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total + "\nVidas: " + vidas);
            }
            if (vidas <= 0) {
                break;
            }
            mode.write(out);
            out.writeUTF(Questions.mediumQuestions[medium[i]].question());
            out.flush();
            fromuser = in.readUTF();
            while (fromuser.isEmpty()) {
                mode.write(out);
                out.writeUTF("");
                fromuser = in.readUTF();
            }
            if (Questions.mediumQuestions[medium[i]].answer().equalsIgnoreCase(fromuser)) {
                total = total + 50;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total);
            } else {
                vidas--;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total + "\nVidas: " + vidas);

            }
            if (vidas <= 0) {
                break;
            }
            mode.write(out);
            out.writeUTF(Questions.hardQuestions[hard[i]].question());
            out.flush();
            fromuser = in.readUTF();
            while (fromuser.isEmpty()) {
                mode.write(out);
                out.writeUTF("");
                fromuser = in.readUTF();
            }
            if (Questions.hardQuestions[hard[i]].answer().equalsIgnoreCase(fromuser)) {
                total = total + 100;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está correta!  \nPontos:" + total);
            } else {
                vidas--;
                mode.read(out);
                out.writeUTF("A resposta " + fromuser.toUpperCase() + " está errada!  \nPontos:" + total + "\nVidas: " + vidas);
            }
            if (vidas <= 0) {
                break;
            }
        }
        if (total > Ranking.getMenor()) {
            mode.write(out);
            out.writeUTF("Você conseguiu um total de " + total + " isto é suficiente para entrar no ranking,\n" +
                    "digite um nome para salvar (máximo 3 letras)");
            fromuser = in.readUTF();
            while (!Naming(fromuser)) {
                mode.write(out);
                out.writeUTF("Nome inválido, digite outro!");
                fromuser = in.readUTF();
            }
            Ranking.adicionar(fromuser.toUpperCase(),total);
            mode.read(out);
            out.writeUTF("Parabens jogador " + fromuser + " Seus pontos foram adicionados ao ranking!");
        }
        else{
            mode.read(out);
            out.writeUTF("Você conseguiu um total de "+ total + " pontos, infelizmente, não foi o suficiente para entrar no ranking!");
        }
        Thread.sleep(3000);
        mode.read(out);
        out.writeUTF("\n\n\nVocê será retornado ao menu.");
        Thread.sleep(5000);

        protocolServer.menu(in,out);


    }

    private static boolean Naming(String fromuser) {
        char caractere;
        int contadorLetras = 0;
        for (int i = 0; i < fromuser.length(); i++) {
            caractere = fromuser.charAt(i);
            if(Character.isLetter(caractere)){
                contadorLetras++;
            }
            if (Character.isWhitespace(caractere))
                return false;

        }
        return contadorLetras == 3;
    }
}
