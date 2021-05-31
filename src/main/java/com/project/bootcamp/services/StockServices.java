package com.project.bootcamp.services;

import com.project.bootcamp.exceptions.BusinessExceptions;
import com.project.bootcamp.exceptions.NotFoundException;
import com.project.bootcamp.mapper.StockMapper;
import com.project.bootcamp.model.Stock;
import com.project.bootcamp.model.dto.StockDTO;
import com.project.bootcamp.repository.StockRepository;
import com.project.bootcamp.util.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockServices {

    @Autowired
    private StockRepository repository;

    @Autowired
    private StockMapper mapper;

    @Transactional
    public StockDTO save(StockDTO dto) {

        Optional<Stock>optionalStock = repository.findByNameAndDate(dto.getName(),dto.getDate());
        if(optionalStock.isPresent()){
            throw new BusinessExceptions(MessageUtils.STOCK_ALREADY_EXISTS);
        }

        Stock stock = mapper.ToEntity(dto);
        repository.save(stock);
        dto.setId(stock.getId());
        return mapper.toDto(stock);

    }
    @Transactional
    public StockDTO update(StockDTO dto) {

        Optional<Stock>optionalStock = repository.findByStockUpdate(dto.getName(),dto.getDate(), dto.getId());
        if(optionalStock.isPresent()){
            throw new BusinessExceptions(MessageUtils.STOCK_ALREADY_EXISTS);
        }

        Stock stock = mapper.ToEntity(dto);
        repository.save(stock);
        dto.setId(stock.getId());
        return mapper.toDto(stock);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<StockDTO> findAll() {
        return mapper.toDto(repository.findAll());
    }



    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public StockDTO findById(long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(NotFoundException::new);

    }
    @Transactional
    public StockDTO delete(long id) {
        StockDTO dto = this.findById(id);
        repository.deleteById(dto.getId());
        return dto;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<StockDTO> findByToday() {
        return repository.findByToday(LocalDate.now()).map(mapper::toDto).orElseThrow(NotFoundException::new);
    }
}