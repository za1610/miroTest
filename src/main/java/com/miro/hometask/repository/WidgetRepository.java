package com.miro.hometask.repository;


import com.miro.hometask.models.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {

    public List<Widget> findAllByOrderByZAsc();

}
