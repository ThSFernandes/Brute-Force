package pack;

import java.io.File;
import java.util.List;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class ChaveiroArquivo {
    private final File arquivoSegredo;
    private String caminhoArquivo;
    private String senhaCorreta; // Armazena a senha correta
    private StringBuilder tentativas; // Armazena as tentativas de senha

    public ChaveiroArquivo(File arquivoSegredo) {
        this.arquivoSegredo = arquivoSegredo;

        if (this.arquivoSegredo != null) {
            // Caminho onde vamos extrair o arquivo compactado
            this.caminhoArquivo = this.arquivoSegredo.getAbsoluteFile().getParent();
        }

        this.senhaCorreta = null;
        this.tentativas = new StringBuilder();
    }

    // Método que permite realizar um teste com a senha passada como parâmetro para um dado arquivo
    public boolean tentaSenha(String senha) {
        try {
            System.out.println("Tentando senha: " + senha);

            ZipFile zipFile = new ZipFile(this.arquivoSegredo);

            // Verifica se o arquivo está criptografado
            if (zipFile.isEncrypted()) {
                // Tentativa da senha aqui
                zipFile.setPassword(senha.toCharArray());
            }
            List<FileHeader> fileHeaderList = zipFile.getFileHeaders();

            // Solução genérica para qualquer tamanho de Header
            for (FileHeader fileHeader : fileHeaderList) {
                // Onde o arquivo será armazenado
                zipFile.extractFile(fileHeader, this.caminhoArquivo);
                // Armazena a senha correta assim que encontrada
                senhaCorreta = senha;
                return true; // Retorna true ao encontrar a senha correta
            }
            return false; // Retorna false caso a senha não seja encontrada

        } catch (ZipException e) {
            // Trata o erro durante a tentativa de senha
            return false;
        }
    }

    // Método que retorna a senha correta (acesso público)
    public String getSenhaCorreta() {
        return senhaCorreta;
    }

    // Método para extrair o arquivo com a senha correta previamente armazenada (acesso público)
    public boolean extrairArquivoComSenhaCorreta() {
        if (senhaCorreta != null) {
            try {
                ZipFile zipFile = new ZipFile(this.arquivoSegredo);
                zipFile.setPassword(senhaCorreta.toCharArray());
                zipFile.extractAll(this.caminhoArquivo);
                return true;
            } catch (ZipException e) {
                // Trata o erro durante a extração do arquivo
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    // Método que retorna todas as tentativas de senha realizadas (acesso público)
    public String getTentativas() {
        return tentativas.toString();
    }
}
