package br.com.faturaweb.fatura.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ItLote")
public class ItLote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdItLote;
	@ManyToOne
	private Lote lote;
	@ManyToOne
	private Lancamento lancamento;	
	
	public ItLote() {
	
	}

	public Long getCdItLote() {
		return cdItLote;
	}

	public void setCdItLote(Long cdItLote) {
		this.cdItLote = cdItLote;
	}

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	@Override
	public String toString() {
		return "ItLote [cdItLote=" + cdItLote + ", lote=" + lote + ", lancamento=" + lancamento + "]";
	}
	
	
	
}
