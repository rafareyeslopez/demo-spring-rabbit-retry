/*
 * Copyright (C) IDB Mobile Technology S.L. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rafael Reyes Lopez
 * @email rafareyeslopez@gmail.com
 * @date 2021-02-22
 *
 */

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
