package org.example.ignitedatagrid.domain.dto;

import java.util.Objects;

public class OrderDto {
    private Long id;
    private Integer volume;
    private Double price;
    private AccountDto account;
    private InstrumentDto instrument;

    public OrderDto(Long id, Integer volume, Double price, AccountDto account, InstrumentDto instrument) {
        this.id = id;
        this.volume = volume;
        this.price = price;
        this.account = account;
        this.instrument = instrument;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }

    public InstrumentDto getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentDto instrument) {
        this.instrument = instrument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) &&
                Objects.equals(volume, orderDto.volume) &&
                Objects.equals(price, orderDto.price) &&
                Objects.equals(account, orderDto.account) &&
                Objects.equals(instrument, orderDto.instrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, volume, price, account, instrument);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", volume=" + volume +
                ", price=" + price +
                ", account=" + account +
                ", instrument=" + instrument +
                '}';
    }
}
