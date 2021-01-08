package com.apirest.bitcoin.validation;

import java.util.Random;

public class DocumentValidation {
    private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_CNPJ = 14;

    private static final int[] PESOS_CPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
    private static final int[] PESOS_CNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };


    public static String cnpjAleatorio() {
        String digitos = digitos(TAMANHO_CNPJ - 2);
        String verificador1 = verificador(digitos, PESOS_CNPJ);
        String verificador2 = verificador(digitos + verificador1, PESOS_CNPJ);
        return digitos + verificador1 + verificador2;
    }

    public static boolean cnpjValido(final String cnpj) {
        return isCnpjFormated(cnpj, TAMANHO_CNPJ, PESOS_CNPJ);
    }

    private static boolean isCnpjFormated(String cnpj, int tamanhoCnpj, int[] pesosCnpj) {
        if (cnpj == null) {
            return false;
        }
        if (cnpj.length() != tamanhoCnpj) {
            return false;
        }
        if (cnpj.matches(cnpj.charAt(0) + "{" + tamanhoCnpj + "}")) {
            return false;
        }
        String digitos = cnpj.substring(0, tamanhoCnpj - 2);
        String verificador1 = verificador(digitos, pesosCnpj);
        String verificador2 = verificador(digitos + verificador1, pesosCnpj);
        return (digitos + verificador1 + verificador2).equals(cnpj);
    }

    public static String cpfAleatorio() {
        String digitos = digitos(TAMANHO_CPF - 2);
        String verificador1 = verificador(digitos, PESOS_CPF);
        String verificador2 = verificador(digitos + verificador1, PESOS_CPF);
        return digitos + verificador1 + verificador2;
    }

    public static boolean cpfValido(final String cpf) {
        return isCnpjFormated(cpf, TAMANHO_CPF, PESOS_CPF);
    }

    /**
     * Utilizado internamente para gerar determinada quantidade de dígitos.
     */
    private static String digitos(int quantidade) {
        StringBuilder digitos = new StringBuilder();
        Random random = new Random();
        for (int contador = 0; contador < quantidade; contador++) {
            digitos.append(random.nextInt(10));
        }
        return digitos.toString();
    }

    /**
     * Utilizado internamente para gerar um dígito verificador.
     */
    private static String verificador(String digitos, int[] pesos) {
        int soma = 0;
        int qtdPesos = pesos.length;
        int qtdDigitos = digitos.length();
        for (int posicao = qtdDigitos - 1; posicao >= 0; posicao--) {
            int digito = Character.getNumericValue(digitos.charAt(posicao));
            soma += digito * pesos[qtdPesos - qtdDigitos + posicao];
        }
        soma = 11 - soma % 11;
        return String.valueOf(soma > 9 ? 0 : soma);
    }

    // Construtor privado, não faz sentido instanciar esta classe
    private DocumentValidation() {}
}
