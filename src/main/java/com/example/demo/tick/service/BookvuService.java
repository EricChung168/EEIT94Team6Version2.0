package com.example.demo.tick.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.tick.bean.BookticketBean;
import com.example.demo.tick.bean.BookticketvuBean;
import com.example.demo.tick.repo.BookTypeRepository;
import com.example.demo.tick.repo.BooktickRepository;
import com.example.demo.tick.repo.BooktickvuRepository;
import com.example.demo.tick.repo.OrderRepository;

@Service
public class BookvuService {


	@Autowired
	private BooktickvuRepository booktickvuRepo;
	@Autowired
	private BooktickRepository booktickRepository;
	
	
	public List<Map.Entry<String, Long>> getTop3PopularMovies() {
        return booktickRepository.getTop3MovieNamesByTicketsNative();
    }
	
	
	
	
	public List<BookticketvuBean> tickfindAll() {

		return booktickvuRepo.findAll();
	}

	public Optional<BookticketvuBean> findtickbyid(Integer id) {
		return booktickvuRepo.findById(id);
	}

	public List<BookticketBean> findseatbyhallid(Integer hallid, Integer showtimeid) {
		return booktickRepository.findSeatidbyHallidShowtimeid(hallid, showtimeid);

	}

	public BookticketBean updatetick(BookticketBean bean) {
		return booktickRepository.save(bean);

	}
	public BookticketBean inser(BookticketBean bean) {
		return booktickRepository.save(bean);
		
	}

	public void deltick(Integer id) {
		booktickRepository.deleteById(id);
	}
	
	public BookticketvuBean findmoneybyid( Integer id) {
		return booktickvuRepo.findbymoney(id);
	}
	
	///////////////////////////// find other
	public List<BookticketvuBean> findother(Integer se, Integer id,Long loid) {

		if (se == 1) {
			return booktickvuRepo.findbyTickid(id);
		} else if (se == 3) {
			return booktickvuRepo.findbyUserid(loid);

		} else if (se == 5) {

			return booktickvuRepo.findbyHallid(id);
		}
		return null;
	}

	public List<BookticketvuBean> findothers(Integer se, String id,Long orderid) {

		if (se == 2) {

			return booktickvuRepo.findbyOrderid(orderid);
		} else if (se == 4) {

			return booktickvuRepo.findbyShowtimedate(id);
		} else if (se == 6) {

			return booktickvuRepo.findbyMoviename(id);
		} else if (se == 7) {

			return booktickvuRepo.findbyPayout(id);
		}
		return null;
	}
	
	public void update(Long orderid) {
		 booktickRepository.updatePayoutByOrderIda(orderid, "Y");
	}
	

}
