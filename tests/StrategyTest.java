/** Casos de teste para cálculo e troca de estratégias de frete. */
public final class StrategyTest {
    private static int verificacoes;
    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
        verificacoes++;
    }
    private static void proximo(double esperado, double atual, String mensagem) {
        verificar(Math.abs(esperado - atual) < 0.000001, mensagem + ": esperado " + esperado + ", recebido " + atual);
    }
    private static void falha(Class<? extends RuntimeException> tipo, Runnable acao, String mensagem) {
        try { acao.run(); }
        catch (RuntimeException e) {
            verificar(tipo.isInstance(e), mensagem + " (tipo de erro inesperado)");
            return;
        }
        throw new AssertionError(mensagem + " (deveria falhar)");
    }
    public static void main(String[] args) {
        Main.Pedido pedido = new Main.Pedido(2.5, Main.Regiao.ESTADUAL);
        Main.CalculadoraFrete calculadora = new Main.CalculadoraFrete(new Main.FreteEconomico());
        proximo(13.0, calculadora.calcular(pedido), "Cálculo econômico");
        verificar("Econômico".equals(calculadora.getNomeEstrategia()), "Estratégia inicial");
        calculadora.definirEstrategia(new Main.FreteExpresso());
        proximo(28.0, calculadora.calcular(pedido), "Cálculo expresso");
        verificar("Expresso".equals(calculadora.getNomeEstrategia()), "Troca para estratégia expressa");
        calculadora.definirEstrategia(new Main.FreteRetirada());
        proximo(0.0, calculadora.calcular(pedido), "Retirada gratuita");
        verificar("Retirada na loja".equals(calculadora.getNomeEstrategia()), "Troca para retirada");

        Main.Pedido leve = new Main.Pedido(1.0, Main.Regiao.NACIONAL);
        proximo(10.0, new Main.FreteEconomico().calcular(leve), "Frete econômico para 1 kg");
        proximo(22.0, new Main.FreteExpresso().calcular(leve), "Frete expresso para 1 kg");
        falha(IllegalArgumentException.class, () -> new Main.Pedido(0, Main.Regiao.MESMA_CIDADE), "Peso zero rejeitado");
        falha(IllegalArgumentException.class, () -> new Main.Pedido(-2, Main.Regiao.NACIONAL), "Peso negativo rejeitado");
        falha(NullPointerException.class, () -> new Main.Pedido(1, null), "Região nula rejeitada");
        falha(NullPointerException.class, () -> calculadora.definirEstrategia(null), "Estratégia nula rejeitada");
        falha(NullPointerException.class, () -> calculadora.calcular(null), "Pedido nulo rejeitado");
        System.out.println(verificacoes + " verificações aprovadas.");
    }
}
