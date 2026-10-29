import java.util.Objects;

/** Demonstra Strategy para calcular frete com políticas intercambiáveis. */
public class Main {
    enum Regiao { MESMA_CIDADE, ESTADUAL, NACIONAL }

    static final class Pedido {
        private final double pesoKg;
        private final Regiao regiao;
        Pedido(double pesoKg, Regiao regiao) {
            if (pesoKg <= 0) throw new IllegalArgumentException("O peso deve ser maior que zero.");
            this.pesoKg = pesoKg;
            this.regiao = Objects.requireNonNull(regiao, "regiao");
        }
        public double getPesoKg() { return pesoKg; }
        public Regiao getRegiao() { return regiao; }
    }

    interface EstrategiaFrete {
        String nome();
        double calcular(Pedido pedido);
    }

    static final class FreteExpresso implements EstrategiaFrete {
        public String nome() { return "Expresso"; }
        public double calcular(Pedido pedido) { return 18.0 + pedido.getPesoKg() * 4.0; }
    }
    static final class FreteEconomico implements EstrategiaFrete {
        public String nome() { return "Econômico"; }
        public double calcular(Pedido pedido) { return 8.0 + pedido.getPesoKg() * 2.0; }
    }
    static final class FreteRetirada implements EstrategiaFrete {
        public String nome() { return "Retirada na loja"; }
        public double calcular(Pedido pedido) { return 0.0; }
    }

    static final class CalculadoraFrete {
        private EstrategiaFrete estrategia;
        CalculadoraFrete(EstrategiaFrete estrategia) { definirEstrategia(estrategia); }
        public void definirEstrategia(EstrategiaFrete estrategia) {
            this.estrategia = Objects.requireNonNull(estrategia, "estrategia");
        }
        public String getNomeEstrategia() { return estrategia.nome(); }
        public double calcular(Pedido pedido) {
            return estrategia.calcular(Objects.requireNonNull(pedido, "pedido"));
        }
    }

    public static void main(String[] args) {
        Pedido pedido = new Pedido(2.5, Regiao.ESTADUAL);
        CalculadoraFrete calculadora = new CalculadoraFrete(new FreteEconomico());
        exibir(calculadora, pedido);
        calculadora.definirEstrategia(new FreteExpresso());
        exibir(calculadora, pedido);
        calculadora.definirEstrategia(new FreteRetirada());
        exibir(calculadora, pedido);
    }

    private static void exibir(CalculadoraFrete calculadora, Pedido pedido) {
        System.out.printf("%s: R$ %.2f%n", calculadora.getNomeEstrategia(), calculadora.calcular(pedido));
    }
}
