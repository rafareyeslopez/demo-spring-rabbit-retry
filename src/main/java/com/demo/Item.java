/*
 * Copyright (C) IDB Mobile Technology S.L. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Rafael Reyes Lopez
 * @email rafareyeslopez@gmail.com
 * @date 2021-02-22
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

	@Id
	@Column(name = "item_id")
	Long id;
	@Column(name = "type")
	String type;
	@Column(name = "value")
	String value;

}
