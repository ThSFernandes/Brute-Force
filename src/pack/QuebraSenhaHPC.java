package pack;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class QuebraSenhaHPC {

    public static void main(String[] args) {
        JFileChooser janela = new JFileChooser();

        // monitora a ação do usuário na árvore de diretório do sistema
        int operacao = janela.showOpenDialog(null);
        if (operacao == JFileChooser.APPROVE_OPTION) {

            // ref. do arquivo selecionado pelo usuário
            File arquivo = janela.getSelectedFile();

            // Verifica se o arquivo é extensão .zip
            if (!arquivo.getAbsolutePath().contains(".zip")) {
                JOptionPane.showMessageDialog(null, "O arquivo selecionado deve ter extensão do tipo .zip", "Arquivo incorreto",
                        JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }

            //------------------Agora é com vocês --------------------
            /* A partir daqui é onde entra a sua estratégia de solução
                Não modifique o código acima, apenas a partir daqui ;)
             */
            // Verificando o número de processadores
            // Buscando quantos processadores tem na máquina
            int numeroCPU = Runtime.getRuntime().availableProcessors();

            // Definindo o que cada trabalhador fará
            int qntTrabalho = (94 / numeroCPU);
            int qntRestante = (94 % numeroCPU);
            int rangeInicial = 33;

            // Instânciando o trabalhadores
            Trabalhador trabs[] = new Trabalhador[numeroCPU];

            // Instânciando as chaves
            //"Chaveiro(s)" começa a trabalhar aqui 
            ChaveiroArquivo chaves = new ChaveiroArquivo(arquivo);

            // Atribui o tempo (milissegundos a variável tempoI)
            long tempoI = System.currentTimeMillis();

            // Definindo o range de cada trabalhador
            for (int i = 0; i < numeroCPU; i++) {
                trabs[i] = new Trabalhador(rangeInicial, rangeInicial + qntTrabalho, chaves, (i + 1));
                rangeInicial += qntTrabalho;

                if (i == numeroCPU - 1) {
                    trabs[i] = new Trabalhador(rangeInicial, rangeInicial + qntTrabalho + qntRestante, chaves, (i + 1));
                }
            }

            // Iniciando os trabalhadores
            for (Trabalhador t : trabs) {
                t.start();
            }

            // Esperando os trabalhadores terminarem
            for (Trabalhador t : trabs) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    System.err.println("ERRO!");
                }
            }

            // Verifica se a senha correta foi encontrada
            if (chaves.getSenhaCorreta() != null) {
                JOptionPane.showMessageDialog(null, "Senha correta achada: " + chaves.getSenhaCorreta());
                // Extrai o arquivo com a senha correta
                if (chaves.extrairArquivoComSenhaCorreta()) {
                    JOptionPane.showMessageDialog(null, "Arquivo extraído com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao extrair o arquivo.", "Erro de extração", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Senha correta não encontrada.", "Senha não encontrada", JOptionPane.WARNING_MESSAGE);
            }

            // Calcula e exibe o tempo de execução
            long tempoF = System.currentTimeMillis();
            long tempoTotal = tempoF - tempoI;

            int horas = (int) (tempoTotal / 3600000);
            int minutos = (int) ((tempoTotal % 3600000) / 60000);
            int segundos = (int) ((tempoTotal % 60000) / 1000);
            int milissegundos = (int) (tempoTotal % 1000);

            JOptionPane.showMessageDialog(null, "Tempo de execução: " + horas + "h, " + minutos + "min, " + segundos + "s, " + milissegundos + "ms");
        }
    }
}
