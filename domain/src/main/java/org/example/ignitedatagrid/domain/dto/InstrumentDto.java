package org.example.ignitedatagrid.domain.dto;

import java.util.Objects;

public class InstrumentDto {
    private Long id;
    private String name;
    private String market;

    public InstrumentDto(Long id, String name, String market) {
        this.id = id;
        this.name = name;
        this.market = market;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentDto that = (InstrumentDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(market, that.market);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, market);
    }

    @Override
    public String toString() {
        return "InstrumentDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", market='" + market + '\'' +
                '}';
    }
}
