package com.fpt.ecoversewasteitem.entities;

import com.fpt.ecoversecommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "waste_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WasteItem extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "correct_bin_code", nullable = false, length = 50)
    private String correctBinCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "created_by", length = 50)
    private String createdBy = "PARTNERSHIP";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correct_bin_code", referencedColumnName = "code", insertable = false, updatable = false)
    private WasteBin wasteBin;
}
