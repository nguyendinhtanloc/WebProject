package com.busbooking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id", updatable = false, nullable = false)
    private Long voucherId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "discount_type", nullable = false)
    private String discountType; // percentage, fixed

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(nullable = false)
    private String status; // active, expired, disabled

    // Constructor
    public Voucher() {}

    // Check if voucher is valid
    @Transient
    public boolean isValid(){
        if (!"active".equals(status)){
            return false;
        }
        LocalDate today = LocalDate.now();
        if (validFrom != null && today.isBefore(validFrom)) return false;
        if (validTo != null && today.isAfter(validTo)) return false;
        return true;
    }

    // Calculate discount amount
    @Transient
    public BigDecimal calculateDiscount(BigDecimal originalAmount){
        if (!isValid()) return BigDecimal.ZERO;

        if ("percentage".equals(discountType)){
            return originalAmount.multiply(discountValue).divide(new BigDecimal(100));
        }else if ("fixed".equals(discountType)){
            return discountValue;
        }
        return BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getVoucherId() { return voucherId; }
    public void setVoucherId(Long voucherId) { this.voucherId = voucherId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }

    public LocalDate getValidTo() { return validTo; }
    public void setValidTo(LocalDate validTo) { this.validTo = validTo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}