import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProcessadorBoletosTest {

    private ProcessadorBoletos processador;

    @BeforeEach()
    public void init() {
        processador = new ProcessadorBoletos();
    }

    @Test
    @DisplayName("deve_verificar_boletos_cuja_soma_de_valores_pagos_é_igual_ao_valor_da_fatura_resulta_fatura_paga")
    public void TestaFaturaPagaParaValoresIguais() {
        Fatura fatura = new Fatura(new Date(), 3000, "Laion Corcino");
        Boleto geladeira = new Boleto(new Date(), 2000);
        Boleto tv = new Boleto(new Date(), 1000);

        List<Boleto> boletos = new ArrayList<>();
        boletos.add(geladeira);
        boletos.add(tv);

        Pagamento pagamento = processador.geraPagamento(boletos);

        fatura.pagaFatura(pagamento);

        assertEquals(fatura.getStatus(), FaturaStatus.PAGO);
        assertEquals(fatura.getValor(), boletos.stream().map(Boleto::getValorPago).reduce(0L, Long::sum));
    }

    @Test
    @DisplayName("deve_verificar_que_boletos_de_soma_de_valores_pagos_maior_que_valor_da_fatura_resulta_em_fatura_paga")
    public void TestaFaturaPagaParaValoresMaiores() {
        Fatura fatura = new Fatura(new Date(), 3000, "Laion Corcino");
        Boleto sofa = new Boleto(new Date(), 5000);
        Boleto notebook = new Boleto(new Date(), 5000);

        List<Boleto> boletos = new ArrayList<>();
        boletos.add(sofa);
        boletos.add(notebook);

        Pagamento pagamento = processador.geraPagamento(boletos);
        fatura.pagaFatura(pagamento);

        assertEquals(fatura.getStatus(), FaturaStatus.PAGO);
        assertNotEquals(fatura.getValor(), boletos.stream().map(Boleto::getValorPago).reduce(0L, Long::sum));
        assertEquals(10000, boletos.stream().map(Boleto::getValorPago).reduce(0L, Long::sum));
    }

    @Test
    @DisplayName("deve_verificar_boletos_onde_soma_de_valores_pagos_for_menor_que_valor_da_fatura_resulta_em_fatura_paga")
    public void TestaFaturaPagaParaValoresMenores() {
        Fatura fatura = new Fatura(new Date(), 3000, "Laion Corcino");
        Boleto microondas = new Boleto(new Date(), 500);
        Boleto forno = new Boleto(new Date(), 500);

        List<Boleto> boletos = new ArrayList<>();
        boletos.add(microondas);
        boletos.add(forno);

        Pagamento pagamento = processador.geraPagamento(boletos);
        fatura.pagaFatura(pagamento);

        assertEquals(fatura.getStatus(), FaturaStatus.NAO_PAGO);
        assertNotEquals(fatura.getValor(), boletos.stream().map(Boleto::getValorPago).reduce(0L, Long::sum));
        assertEquals(1000, boletos.stream().map(Boleto::getValorPago).reduce(0L, Long::sum));
    }

    @Test
    @DisplayName("deve_garantir_que_todo_pagamento_eh_boleto")
    public void TestaTipoDePagamento() {
        Boleto boletoA = new Boleto(new Date(), 5000);

        List<Boleto> boletos = new ArrayList<>();
        boletos.add(boletoA);

        Pagamento pagamento = processador.geraPagamento(boletos);

        assertEquals(pagamento.getFormaPagamento(), FormaPagamento.BOLETO);
        assertNotEquals(pagamento.getFormaPagamento(), FormaPagamento.CARTAO);
    }

}
