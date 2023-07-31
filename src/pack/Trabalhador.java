package pack;

/**
 *
 * @author Thiago e Paloma
 */
public class Trabalhador extends Thread {

    private static final int RANGE_INICIAL = 33;
    private static final int RANGE_FINAL = 126;
    private static final int TAMANHO_SENHA = 5;

    private final ChaveiroArquivo chaveiro;
    private final int indiceInicial;
    private static String senhaCorreta;

    private static volatile boolean senhaEncontrada;

    public Trabalhador(int indiceInicial, int indiceFinal, ChaveiroArquivo chaveiro, int numcpu) {
        this.indiceInicial = indiceInicial;
        this.chaveiro = chaveiro;
    }

    public static String getSenhaCorreta() {
        return senhaCorreta;
    }

    @Override
    public void run() {

        //Criando uma variável para armazenar a quantidade de tentativas
        int limite = (int) Math.pow(RANGE_FINAL - RANGE_INICIAL + 1, TAMANHO_SENHA);
        
        //Criando a variável que receberá as tentativas de senhas
        StringBuilder senhaTesteString = new StringBuilder(TAMANHO_SENHA);

        // senha inicial com todos os caracteres iguais ao primeiro caracter do range
        int[] senhaTeste ={indiceInicial,33,33,33,33};
  
        //Fazendo a condição de verificação do loop
        for (int i = 0; i < limite && !senhaEncontrada; ++i) {
            senhaTesteString.setLength(0);
            for (int j = 0; j < TAMANHO_SENHA; j++) {

                senhaTesteString.append((char) senhaTeste[j]);
            }
            /*Caso queira visualizar a senha
             System.out.println( senhaTesteString);
            */
            
            

            // Testa a senha atual
            if (chaveiro.tentaSenha(senhaTesteString.toString())) {
                senhaCorreta = senhaTesteString.toString(); 
                senhaEncontrada = true;    
            } 

            // Incrementa a senha atual
            senhaTeste[TAMANHO_SENHA - 1]++;
            for (int j = TAMANHO_SENHA - 1; j >= 0; j--) {
                
                
                if (senhaTeste[j] > RANGE_FINAL) {
                    senhaTeste[j] = RANGE_INICIAL;

                    if (j > 0) {

                        senhaTeste[j - 1]++;
                    }
                }
            }
        }

    }
}