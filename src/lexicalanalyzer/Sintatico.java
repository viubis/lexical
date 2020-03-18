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
        if(verificaTokenLexema("const")||verificaTokenLexema("struct")||verificaTokenLexema("var")
                ||verificaTokenLexema("function")||verificaTokenLexema("procedure")||verificaTokenLexema("start")){
            if(verificaTokenLexema("const")){
                Const();
                token=this.seguinte();
                if(verificaTokenLexema("struct")){
                    Struct();
                    token=this.seguinte();
                    if(verificaTokenLexema("var")){
                        variaveis();
                        token=this.seguinte();
                        while(verificaTokenLexema("function")||verificaTokenLexema("procedure")){
                            funcaoprocedure();
                            token=this.seguinte();
                        }
                        if(verificaTokenLexema("start")){
                            Start();
                        }
                    }  
                }
            }
        }
    }


    public void funcaoprocedure(){
        if(verificaTokenLexema("function")){
            token=this.seguinte();
            if(tipo()==true){
                token=this.seguinte();
                if(verificaTokenTipo("identificador")){
                    token=this.seguinte();
                    if(verificaTokenLexema("(")){
                        parametro();
                    }
                }       
            }
        }else if(verificaTokenLexema("procedure")){
            token=this.seguinte();
                if(verificaTokenTipo("identificador")){
                    token=this.seguinte();
                    if(verificaTokenLexema("(")){
                        parametro();
                    }
                }
        }else
            return;
    }    
    
    private void parametro(){
        token=this.seguinte();
        if(tipo()== true){
            token = this.seguinte();
            if(verificaTokenTipo("identificador")){
                token=this.seguinte();
                if(verificaTokenLexema("[")){
                    token = this.seguinte();
                    if(verificaTokenLexema("]")){
                        token=this.seguinte();
                        if(verificaTokenLexema("[")){
                             token = this.seguinte();
                            if(verificaTokenLexema("]")){
                                token=this.seguinte();
                                if(verificaTokenLexema(",")){
                                    parametro();
                                }else if(verificaTokenLexema(")")){
                                    token=this.seguinte();
                                    if(verificaTokenLexema("{")){
                                        corpo();
                                    }
                                }
                            }
                         }else{
                            if(verificaTokenLexema(",")){
                                parametro();
                            }else if(verificaTokenLexema(")")){
                                token=this.seguinte();
                                if(verificaTokenLexema("{")){
                                    corpo();
                                }
                            }
                        }
                    }
                }else{
                    if(verificaTokenLexema(",")){
                        parametro();
                    }else if(verificaTokenLexema(")")){
                        token=this.seguinte();
                        if(verificaTokenLexema("{")){
                            corpo();
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
                token=this.seguinte();
                while(tipo()==true){
                    token= this.seguinte();
                    if(verificaTokenTipo("Id")){
                        token=this.seguinte();
                        if(valor()==true){
                            token=this.seguinte();
                            if(verificaTokenLexema(",")){
                                while(verificaTokenLexema(",")){
                                    token=this.seguinte();
                                     if(verificaTokenTipo("Id")){
                                        token=this.seguinte();
                                        if(valor()==true){
                                            //certo ate aqui
                                        }else
                                            adicionarErro("Sem valor definido");
                                    }else
                                         adicionarErro("Identificador invalido");
                                }while(verificaTokenLexema(";")){
                                        token= this.seguinte();
                                        if(verificaTokenTipo("Id")){
                                            token=this.seguinte();
                                            if(valor()==true){
                                                token=this.seguinte();
                                                if(verificaTokenLexema(",")){
                                                    while(verificaTokenLexema(",")){
                                                    token=this.seguinte();
                                                        if(verificaTokenTipo("Id")){
                                                            token=this.seguinte();
                                                            if(valor()==true){
                                                                //certo ate aqui
                                                            }
                                                        }
                                                    }        
                                                }

                                            }    
                                        }
                                }
                            }
                        }
                    }
                
                }
                token=seguinte();
                if(verificaTokenLexema("}")){
                    //certo
                }
            }
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
                        }
                    }
                }            
            }
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
                                }
                            }
                        }
                    }else if(verificaTokenLexema("{")){
                        contStruct();
                        if(verificaTokenLexema("}")){
                                    //certo
                                }
                    }
                }
            }
        }
    }
    

    public void contStruct(){
        token=this.seguinte();
        while(tipo()==true){
            token=this.seguinte();
            if(verificaTokenTipo("Id")){
                token= this.seguinte();
                if(verificaTokenLexema(",")){
                    
                }else if(verificaTokenLexema(";")){
                    token=this.seguinte();
                    if(verificaTokenLexema("}")){
                        return;
                    }
                }
            }
        }
    }
    
    public boolean tipo(){
        if(token.getLexema()== "int" ||token.getLexema()== "boolean" ||token.getLexema()== "string" ||"real"== token.getLexema()){
            return true;
        }else if(verificaTokenTipo("Id")){
            return true;
        }
    
        return false;
    }
    
    public boolean valor(){
        if(verificaTokenTipo("numero")||verificaTokenTipo("identificador")||verificaTokenTipo("cadeiaDeCaractere")
                ||verificaTokenLexema("true")||verificaTokenLexema("false")){
            return true;
        }
        return false;
    }
    
    public void variaveis() {
        if (verificaTokenLexema("var")) {
            token = this.seguinte();
            if (verificaTokenLexema("{")) {
                complementoVariaveis();
                token = this.seguinte();
                if (verificaTokenLexema("}")) {
                    //certo
                }
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
            } 
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
    
    
    public void read(){
        if(verificaTokenLexema("read")){
            token=this.seguinte();
            if(verificaTokenLexema("(")){
                identificadorsemfuncao();
                token=this.seguinte();
                if(verificaTokenLexema(",")||verificaTokenLexema(")")){
                    if(verificaTokenLexema(",")){
                        while(verificaTokenLexema(",")){
                            identificadorsemfuncao();
                            token=this.seguinte();
                        }
                        if(verificaTokenLexema(")")){
                            token=this.seguinte();
                            if(verificaTokenLexema(";")){
                                //certo
                            }
                        } 
                    }else if(verificaTokenLexema(")")){
                        token=this.seguinte();
                        if(verificaTokenLexema(";")){
                            //certo
                        }
                    } 
                }
            }
        }
    }
    
    public void identificadorsemfuncao(){
        token=this.seguinte();
        if(verificaTokenLexema("local")||verificaTokenLexema("global")){
            token=this.seguinte();
            if(verificaTokenLexema(".")){
                token=this.seguinte();
                if(verificaTokenTipo("identificador")){
                    identificador2();
                }
            }
        }else if(verificaTokenTipo("identificador")){
            identificador2();
        }
    }
   
    public void identificador2(){
        
    }

    public void Print(){
        if(verificaTokenLexema("print")){
            token=this.seguinte();
            if(verificaTokenLexema("(")){
                token=this.seguinte();
                if(verificaTokenTipo("string")||verificaTokenTipo("numero")||verificaTokenTipo("identificador")){
                    token=this.seguinte();
                    while(verificaTokenLexema(",")){
                        token=this.seguinte();
                        if(verificaTokenTipo("string")||verificaTokenTipo("numero")||verificaTokenTipo("identificador")){
                            token=this.seguinte();
                   
                        }
                    }
                    if(verificaTokenLexema(")")){
                        if(verificaTokenLexema(";")){
                            //certo
                        }
                    }
               }
            }
        }else{
            
        }
    return;
    }
    
    public void If() {
        if (verificaTokenLexema("if")) {
            token = seguinte();
            if (verificaTokenLexema("(")) {
                token = this.seguinte();
                condicional();
                if (verificaTokenLexema(")")) {
                    token = this.seguinte();
                    if (verificaTokenLexema("then")){
                            token = this.seguinte();                   
                        if (verificaTokenLexema("{")) {
                            token = this.seguinte();
                            if (verificaTokenLexema("}")) {
                                //escreva correto
                            }
                        }
                    }
                } 

            } 

        }
        return;
    }
    
     private void condicional() {
        if (verificaTokenTipo("int") || verificaTokenTipo("identificador")) {
            token = this.seguinte();
            if (verificaTokenTipo("opRelacional") || verificaTokenTipo("opLogico")) {
                token = this.seguinte();
                if (verificaTokenTipo("int") || verificaTokenTipo("identificador")) {
                    token = this.seguinte();
                }
            } 
        } 
        return;
    }

    public void vetor() {
        token = this.seguinte();
        if (verificaTokenLexema("[")) {
            token = this.seguinte();
            if (verificaTokenTipo("int") || verificaTokenTipo("identificador")) {
                opIndice();
                token = this.seguinte();
                if (verificaTokenLexema("]")) {
                    matriz();
                }
            } 
        } else {
            return;
        }
    }

    public void opIndice() {
        token = this.seguinte();
        if (verificaTokenTipo("opAritimetico")) {
            token = this.seguinte();
            if (verificaTokenTipo("int") || verificaTokenTipo("identificador")) {
                opIndice();
            } 
        } else {
            return;
        }

    }
    public void matriz(){
    token = this.seguinte();
        if (verificaTokenLexema("[")) {
            token = this.seguinte();
            if (verificaTokenTipo("int") || verificaTokenTipo("identificador")) {
                opIndice();
                token = this.seguinte();
                if (verificaTokenLexema("]")) {
                    return;
                }
            }
        } else {
            return;
        }
    
    }  
    
     public void incremento() {
        token = this.seguinte();
        if (verificaTokenLexema("(")) {
            token = this.seguinte();
            if (verificaTokenLexema("++") || verificaTokenLexema("--")) {
                token = this.seguinte();
                if (verificaTokenTipo("identificador")) {
                    vetor();
                    //
                }
            } else if (verificaTokenTipo("identificador")) {
                vetor();
                token = this.seguinte();
                if (verificaTokenLexema("++") || verificaTokenLexema("--")) {
                    //
                }
            } else if (verificaTokenLexema("true") || verificaTokenLexema("false")) {
                //
            } else if (verificaTokenLexema("!")) {
                if (verificaTokenTipo("identificador")) {
                    //
                } else if (verificaTokenLexema("true") || verificaTokenLexema("false")) {
                    //
                }
            }
        } else if (verificaTokenLexema("++") || verificaTokenLexema("--")) {
            token = this.seguinte();
            if (verificaTokenTipo("identificador")) {
                vetor();
            }
        } else if (verificaTokenTipo("identificador")) {
            vetor();
            token = this.seguinte();
            if (verificaTokenLexema("++") || verificaTokenLexema("--")) {

            }
        } else if (verificaTokenLexema("true") || verificaTokenLexema("false")) {
            //
        } else if (verificaTokenLexema("!")) {
            if (verificaTokenTipo("identificador")) {
                //
            } else if (verificaTokenLexema("true") || verificaTokenLexema("false")) {
                //
            }
        }
    }
     
    public void atribuicaoVariavel(){
        token = this.seguinte();
        if(verificaTokenTipo("identificador")){
            vetor();
            if(verificaTokenLexema("=")){
                verificaCaso();
            }else{}
        }else{}
    }
    
    public void verificaCaso(){
        incremento();
   
    }
    
    public void corpo(){
        token=this.seguinte();
        if(verificaTokenLexema("var")){
            variaveis();
            token=this.seguinte();
            while(comandos()==true){
                if(verificaTokenLexema("if")){
                    If();
                }else if(verificaTokenLexema("while")){
                    repeticaolaco();
                }else if(verificaTokenLexema("read")){
                    read();
                }else if(verificaTokenLexema("print")){
                    Print();
                }else if(verificaTokenLexema("return")){
                    comandosReturn();
                }else if(verificaTokenLexema("local")||verificaTokenLexema("global")||verificaTokenTipo("identificador")){
                    identificadorsemfuncao();
                }
                token=this.seguinte();
            }
            if(verificaTokenLexema("}")){
                //certo
            }
        }else if(verificaTokenLexema("}")){
            //certo
            return;
        }
    }
    
    public boolean comandos(){
        if(verificaTokenLexema("if")){
           return true;
        }else if(verificaTokenLexema("while")){
            return true;
        }else if(verificaTokenLexema("read")){
            return true;
        }else if(verificaTokenLexema("print")){
            return true;
        }else if(verificaTokenLexema("return")){
            return true;
        }else if(verificaTokenLexema("local")||verificaTokenLexema("global")||verificaTokenTipo("identificador")){
             return true;
        }
        return false;
    }
    

    public boolean incrementoReturn() {
         if (verificaTokenLexema("++") || verificaTokenLexema("--")) {
            token = this.seguinte();
            if (verificaTokenTipo("identificador")) {
                return true;
            }
        } else if (verificaTokenTipo("identificador")) {
            token = this.seguinte();
            if (verificaTokenLexema("++") || verificaTokenLexema("--")) {
                return true;
            }
        } else if (verificaTokenLexema("true") || verificaTokenLexema("false")) {
            return true;
        }
         return false;
    }
    
    public void comandosReturn(){
        token=this.seguinte();
        if(verificaTokenLexema("return")){
            token=this.seguinte();
            if(verificaTokenLexema(";")){
                //certo
            }else if(incrementoReturn()==true){
                token=this.seguinte();
                if(verificaTokenLexema(";")){
                    //certo
                }
            }
        }
    }
}
