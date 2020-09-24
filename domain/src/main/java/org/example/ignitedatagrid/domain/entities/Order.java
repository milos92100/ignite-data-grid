package org.example.ignitedatagrid.domain.entities;

import java.util.Objects;

public class Order {
    private Long id;
    private Integer volume;
    private Double price;
    private Long accountId;
    private Long instrumentId;

    public Order(Long id, Integer volume, Double price, Long accountId, Long instrumentId) {
        this.id = id;
        this.volume = volume;
        this.price = price;
        this.accountId = accountId;
        this.instrumentId = instrumentId;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(volume, order.volume) &&
                Objects.equals(price, order.price) &&
                Objects.equals(accountId, order.accountId) &&
                Objects.equals(instrumentId, order.instrumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, volume, price, accountId, instrumentId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", volume=" + volume +
                ", price=" + price +
                ", accountId=" + accountId +
                ", instrumentId=" + instrumentId +
                '}';
    }
}
