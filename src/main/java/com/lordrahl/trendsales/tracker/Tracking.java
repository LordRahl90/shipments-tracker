package com.lordrahl.trendsales.tracker;

import com.lordrahl.trendsales.shipments.PackageStatus;
import com.lordrahl.trendsales.shipments.Shipment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"packageNumber", "packageStatus"})})
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String packageNumber;

    private PackageStatus packageStatus;

    @CreationTimestamp
    private Date createdAt;
}
