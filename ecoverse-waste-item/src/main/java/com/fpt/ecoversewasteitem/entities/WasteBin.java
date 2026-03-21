package com.fpt.ecoversewasteitem.entities;

import com.fpt.ecoversecommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "waste_bins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WasteBin extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "color_hex", nullable = false, length = 7)
    private String colorHex;

    @Column(name = "icon_url", length = 512)
    private String iconUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @Column(name = "active")
    private Boolean active = true;
}
