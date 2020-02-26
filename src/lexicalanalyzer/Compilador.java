/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalanalyzer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Victor e Pedro
 */
public class Compilador {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        LeituraArquivo arquivo = new LeituraArquivo();
        Automato analiseLexica;
        ArrayList<String> codigos = new ArrayList<>();
        codigos = arquivo.leitura();

        if (codigos.isEmpty()) {
            System.out.println("Coloque o arquivo de teste na pasta de entrada");
            System.exit(0);
        }
        for (String codigo : codigos) {

            analiseLexica = new Automato();
            ArrayList<String> codigoFonte = new ArrayList<>();
            codigoFonte = arquivo.lerArquivo(codigo);

            analiseLexica.analisadorLexico(codigoFonte);

            //removendo os tokens de comentarios
            ArrayList<Token> tokens = new ArrayList<>();
            Iterator it = analiseLexica.getListarTokens().iterator();
            while (it.hasNext()) {
                Token t = (Token) it.next();
                if (!t.getTipo().equals("comentario")) {
                    tokens.add(t);
                }
            }

            //fazendo análise sintatica
            Sintatico as = new Sintatico(tokens);
            as.iniciar();
            if (!as.possuiErros()) {
                arquivo.naoContemErros();
            } else {
                arquivo.escreverSintatico(as.getListarTokensErrados(), as.erros());
            }
            System.out.println("Analise sintatica concluida");

        }
    }

}