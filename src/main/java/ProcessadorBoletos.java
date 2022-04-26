import java.util.Date;
import java.util.List;

public class ProcessadorBoletos {

    public Pagamento geraPagamento(List<Boleto> boletos) {
        long total = boletos
                .stream()
                .mapToLong(Boleto::getValorPago)
                .reduce(0, Long::sum);

        return new Pagamento(total, new Date(), FormaPagamento.BOLETO);
    }

}
