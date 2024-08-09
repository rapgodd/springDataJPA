package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Persistable;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;


    @Test
    public void save() {
        Item item = new Item("a");
        itemRepository.save(item);
    }


}