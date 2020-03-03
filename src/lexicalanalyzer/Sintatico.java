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
                            if(verificaTokenLexema("53")){
                                adicionarErro("depois do fim do programa contém tokens");
                            }else{
                                //certo
                            }
                        }else{
                            adicionarErro("Falta o 'start'");
                        }
                    }else{
                        adicionarErro("Falta o 'var");
                    }  
                }else{
                    adicionarErro("falta a 'struct'");
                }
            }else{
                adicionarErro("falta o 'const'");
            }
        }else{
            adicionarErro("Programa não inicia com nenhuma das palavras reservadas");
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
                    }else
                        adicionarErro("falta (");
                }else
                    adicionarErro("identificador invalido");
                        
            }else
                adicionarErro("tipo invalido");
        }else if(verificaTokenLexema("procedure")){
            token=this.seguinte();
                if(verificaTokenTipo("identificador")){
                    token=this.seguinte();
                    if(verificaTokenLexema("(")){
                        parametro();
                    }else
                        adicionarErro("falta (");
                }else
                    adicionarErro("identificador invalido");
                
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
                    }else
                        adicionarErro("falta ]");
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
                    
            }else
                adicionarErro("identificador invalido");
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
                                                        }else
                                                            adicionarErro("Sem valor definido");
                                                        }else
                                                            adicionarErro("Identificador invalido");
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
                }else{
                    adicionarErro("falta }");
                }
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
                        }else{
                            adicionarErro("falta }");
                        }
                    }else{
                        adicionarErro("falta {");
                    }    
                }else{
                    adicionarErro("falta )");
                }
            }else{
                adicionarErro("falta (");
            }
        }else{
            adicionarErro("erro palavra n reconhecida");
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
                            } else{
                                adicionarErro("faltou ;");
                            }
                        } else{
                            adicionarErro("faltou )");
                        }
                    }else if(verificaTokenLexema(")")){
                        token=this.seguinte();
                        if(verificaTokenLexema(";")){
                            //certo
                        }else{
                            adicionarErro("faltou ;");
                        }
                    } else{
                        adicionarErro("faltou )");
                    }
                }else{
                    adicionarErro("Esperado ')' ou ','");
                }
            }else{
                adicionarErro("faltou (");
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
                }else{
                    adicionarErro("identificador nao valido");
                }
            }else{
                adicionarErro("falta .");
            }
        }else if(verificaTokenTipo("identificador")){
            identificador2();
        }else{
            adicionarErro("identificador nao valido");
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
                        }else{
                            adicionarErro("falta ;");
                        }
                    }else{
                        adicionarErro("falta )");
                    }
               }else{
                    adicionarErro("erro tem de ser 'String' , 'numero' ou Identificador");
                }
            }else{
                adicionarErro("falta (");
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
                            } else {
                                adicionarErro("faltou '}'");
                            }
                        } else {
                            adicionarErro("faltou '{'");
                        }
                    }else{
                        adicionarErro("Faltou 'then'");
                    }
                } else {
                    adicionarErro("faltou ')'");
                }

            } else {
                adicionarErro("faltou '('");
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
                } else{
                    adicionarErro("faltou o segundo argumento da condicao");
                }
            } else{
                adicionarErro("faltou a condicao");
            }
        } else {
            adicionarErro("faltou o primeiro argumento da condicao");
        }
        return;
    }
//<OpIndice> ::= OperadoresAritmeticos <OpI2> <OpIndice> | <>
//<OpI2> ::= Numeros | Identificadores  
//<Vetor> ::= '[' <OpI2><OpIndice> ']' <Matriz> | <>
//<Matriz> ::= '[' <OpI2><OpIndice> ']' | <>

    public void vetor() {
        token = this.seguinte();
        if (verificaTokenLexema("[")) {
            token = this.seguinte();
            if (verificaTokenTipo("int") || verificaTokenTipo("identificador")) {
                opIndice();
                token = this.seguinte();
                if (verificaTokenLexema("]")) {
                    matriz();
                } else {
                    adicionarErro("faltou ]");
                }
            } else {
                adicionarErro("faltou indice");
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
            } else {
                adicionarErro("faltou o operando");
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
                } else {
                    adicionarErro("faltou ]");
                }
            } else {
                adicionarErro("faltou o indice");
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
    
    public void corpoteste(){
        token=this.seguinte();
        if(verificaTokenLexema("var")){
            variaveis();
        }else if(verificaTokenLexema("}")){
            //certo
            return;
        }
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
                }else 
                    adicionarErro("falta ;");
                
            }else
                adicionarErro("comando invalido");
        }
    }
}
