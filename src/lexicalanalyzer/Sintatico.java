/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexicalanalyzer;

import java.util.ArrayList;
import java.util.Iterator;
import static jdk.nashorn.internal.parser.TokenType.EOF;

/**
 *
 * @author Victor Souza e Pedro Brandão
 */
public class Sintatico {
    private Token token;
    private ArrayList<Token> listaTokens;
    private int posicaoTokenFinal;
    private int posicaoTokenAtual;
    private ArrayList<String> erros;
    private ArrayList<Token> listaTokensErrados;
    private Iterator iterador;
    
    
    Sintatico(ArrayList<Token> tokens) {
        this.listaTokens = tokens;
        this.posicaoTokenFinal = listaTokens.size();
        this.iterador = listaTokens.iterator();
        this.posicaoTokenAtual = 0;
        this.erros = new ArrayList<String>();
        this.listaTokensErrados = new ArrayList<Token>();
        
    }
    
    void iniciar() {
        programa();
    }
    
    public Token seguinte(){
        int atual = posicaoTokenAtual;
        int ultimo = posicaoTokenFinal;
        
        if(atual < ultimo){
            posicaoTokenAtual++;
            return listaTokens.get(atual);
        }
        
        return null;
    }
    
    public Token primeiro(){
        int atual = posicaoTokenAtual;
        int ultimo = posicaoTokenFinal;
        
        if(atual < ultimo){
            return listaTokens.get(atual);
        }
        
        return null;
    }

    public boolean verificaTokenLexema (String a){
        if(primeiro().getLexema().equals(a))
            return true;
        return false;
    }
    public boolean verificaTokenTipo (String a){
        if(primeiro().getTipo().equals(a))
            return true;
        return false;
    }
    public ArrayList<Token> getListarTokensErrados() {
        return listaTokensErrados;
    }

    public ArrayList<String> erros() {
        return erros;
    }

    public void adicionarErro(String e) {
        if (seguinte() != null) {
            listaTokensErrados.add(seguinte());
            erros.add(e);
        } else {
            listaTokensErrados.add(token);
            erros.add(e);
        }
    }

    public boolean possuiErros() {
        if (!listaTokensErrados.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean encontrarToken(String t, int i) {
        boolean aux = false;
        if (seguinte() == null) {
            return false;
        }
        if (i == 0) {
            while ((!seguinte().getLexema().equals(t))) {
                token = primeiro();
                aux = true;
            }
        } else {
            while ((!seguinte().getTipo().equals(t))) {
                token = primeiro();
                aux = true;
            }
        }

        return aux;
    }
    
    public void programa(){
        if(verificaTokenLexema("const")){
            Const();
            token=this.seguinte();
            if(verificaTokenLexema("struct")){
                //Struct();
                token=this.seguinte();
                if(verificaTokenLexema("var")){
                    variaveis();
                    token=this.seguinte();
                    if(verificaTokenLexema("function")||verificaTokenLexema("procedura")){
                        //funcaoprocedura();
                        token=this.seguinte();
                        if(verificaTokenLexema("start")){
                            Start();
                            if(verificaTokenLexema("53")){
                                adicionarErro("depois do fim do programa contém tokens");
                            }else{
                                adicionarErro("Sucesso");
                            }
                        }
                    }else{
                        if(verificaTokenLexema("start")){
                            Start();
                            if(verificaTokenLexema("53")){
                                adicionarErro("depois do fim do programa contém tokens");
                            }else{
                                adicionarErro("Sucesso");
                            }
                        }
                    }
                }
            }
        }else{
            if(verificaTokenLexema("struct")){
                //Struct();
                token=this.seguinte();
                if(verificaTokenLexema("var")){
                    variaveis();
                    token=this.seguinte();
                    if(verificaTokenLexema("function")||verificaTokenLexema("procedura")){
                        //funcaoprocedura();
                        token=this.seguinte();
                        if(verificaTokenLexema("start")){
                            Start();
                            if(verificaTokenLexema("53")){
                                adicionarErro("depois do fim do programa contém tokens");
                            }else{
                                adicionarErro("Sucesso");
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void Const(){
        if(verificaTokenLexema("const")){
            token= this.seguinte();
            if(verificaTokenLexema("{")){
                //tipoConst();
                token= this.seguinte();
                if(verificaTokenLexema("}")){
                    // certo
                }else
                    adicionarErro("faltou }");
            }else
                adicionarErro("faltou {");
        } 
    }

    
    public void Start(){
        if(verificaTokenLexema("start")){
            token=this.seguinte();
            if(verificaTokenLexema("(")){
                if(verificaTokenLexema(")")){
                    token=this.seguinte();
                    if(verificaTokenLexema("{")){
                        //corpo();
                        token=this.seguinte();
                        if(verificaTokenLexema("}")){
                         //certo
                        }else{
                            adicionarErro("faltou }");
                        }
                    }else{
                        adicionarErro("faltou {");
                    }
                }else{
                    adicionarErro("faltou )");
                }            
            }else{
                adicionarErro("faltou (");
            }
        }else{
            adicionarErro("faltou start");
        }
    }
    
    public void Struct(){
        if(verificaTokenLexema("typedef")){
            token=this.seguinte();
            if(verificaTokenLexema("struct")){
                token=this.seguinte();
                if(verificaTokenTipo("Id")){
                    token=this.seguinte();
                    if(verificaTokenLexema("extends")){
                        token=this.seguinte();
                        if(verificaTokenTipo("Id")){
                            token=this.seguinte();
                            if(verificaTokenLexema("{")){
                                contStruct();
                                if(verificaTokenLexema("}")){
                                    //certo
                                }else{
                                    adicionarErro("erro não contem }");
                                }
                            }else{
                                adicionarErro("erro não contem {");
                            }
                        }else{
                            adicionarErro("erro não contem identificador valido");
                        }
                    }else if(verificaTokenLexema("{")){
                        contStruct();
                        if(verificaTokenLexema("}")){
                                    //certo
                                }
                    }else{
                        adicionarErro("Erro, não contem nem 'Extends' nem {");
                    }
                }else{
                    adicionarErro("erro não contem identificador valido");
                }
            }else{
                adicionarErro("erro não contem struct");
            }
        }else{
            adicionarErro("erro");
        }
    }
    
    public void contStruct(){
        
    }
    
    public void variaveis() {
        if (verificaTokenLexema("var")) {
            token = this.seguinte();
            if (verificaTokenLexema("{")) {
                complementoVariaveis();
                token = this.seguinte();
                if (verificaTokenLexema("}")) {
                    //certo
                } else {
                    adicionarErro("faltou }");
                }
            } else {
                adicionarErro("faltou {");
            }
        } else {
            return;
        }
    }

    public void complementoVariaveis() {
        token = this.seguinte();
        if (verificaTokenLexema("int") || verificaTokenLexema("boolean") || verificaTokenLexema("string")
                || verificaTokenLexema("real")) {
            estruturaVariaveis();
            complementoVariaveis();
        } else {
            return;
        }        
    }

    public void estruturaVariaveis() {
        token = this.seguinte();
        if (verificaTokenTipo("identificador")) {
            token = this.seguinte();
            if (verificaTokenLexema(";")) {
                return;
            } else if (verificaTokenLexema(",")) {
                estruturaVariaveis();
            } else {
                adicionarErro("faltou ;");
            }
        } else {
            adicionarErro("faltou id");
        }
        
    }
    
    public void repeticaolaco(){
        if(verificaTokenLexema("while")){
            token=this.seguinte();
            if(verificaTokenLexema("(")){
                //expressaologicarelacional();
                token=this.seguinte();
                if(verificaTokenLexema(")")){
                    token=this.seguinte();
                    if(verificaTokenLexema("{")){
                        //corpo();
                        token=this.seguinte();
                        if(verificaTokenLexema("}")){
                            //certo
                        }
                    }
                        
                }
            }
        }
    }
}
