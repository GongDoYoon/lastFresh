package com.example.lastfresh.controller.rider;


import com.example.lastfresh.domain.dto.BillProductDTO;
import com.example.lastfresh.domain.repository.BillProductRepository;
import com.example.lastfresh.domain.repository.ReviewRepository;
import com.example.lastfresh.domain.vo.ProductVO;
import com.example.lastfresh.service.rider.RiderService;
import com.example.lastfresh.service.user.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rider/*")
public class RiderController {
    @Autowired
    private RiderService riderService;
    private final ReviewService reviewService;
    private BillProductRepository billProductRepository;
    private final ReviewRepository reviewRepository;
    @GetMapping("/riderList")
    public void riderList(Model model){
        List<BillProductDTO> bills= riderService.getList();
        model.addAttribute("orders", bills);
    }

    @GetMapping("/riderMy")
    public void riderMy(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userNum = Long.valueOf(String.valueOf(session.getAttribute("userNumber")));

        List<BillProductDTO> bills= riderService.getMyList(userNum);
        model.addAttribute("myOrders", bills);
    }
    @GetMapping("/riderListF")
    public void riderFilter(Model model,String sido,String sigungu,String dong){
        String sellProductAddress=sido+" "+sigungu+" "+dong;
        log.info(sellProductAddress);

        List<BillProductDTO> bills= riderService.selectFilter(sellProductAddress);
        model.addAttribute("orders", bills);
    }

    @PostMapping("/upDateStatusToTwo")
    public RedirectView upDateStatusToTwo(BillProductDTO billProductDTO, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userNum = Long.valueOf(String.valueOf(session.getAttribute("userNumber")));
        billProductDTO.setUserNum(userNum);

        riderService.upDateStatusToTwo(billProductDTO);
        return new RedirectView("riderList");
    }
    @RequestMapping("/upDateStatusToThree")
    public RedirectView upDateStatusToThree(Long billProductListNum,Long sellProductNum, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userNum = Long.valueOf(String.valueOf(session.getAttribute("userNumber")));
        BillProductDTO billProductDTO=new BillProductDTO();
        billProductDTO.setUserNum(userNum);
        billProductDTO.setBillProductListNum(billProductListNum);

        reviewService.insert(userNum, sellProductNum);
        riderService.upDateStatusToThree(billProductDTO);

        return new RedirectView("riderMy");
    }
    @RequestMapping("/upDateStatusToMinus")
    public RedirectView upDateStatusToMinus(Long billProductListNum, HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userNum = Long.valueOf(String.valueOf(session.getAttribute("userNumber")));
        BillProductDTO billProductDTO=new BillProductDTO();
        billProductDTO.setUserNum(userNum);
        billProductDTO.setBillProductListNum(billProductListNum);

        riderService.upDateStatusToMinus(billProductDTO);

        log.info("`````````````````````````````````````````````````");
        log.info("들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴ㅍ");
        log.info("들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴ㅍ");
        log.info(String.valueOf(billProductDTO.getBillProductListNum()));
        log.info( billProductDTO.toString());
        log.info("들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴ㅍ");
        log.info("들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴들어옴ㅍ");
        log.info("`````````````````````````````````````````````````");
        return new RedirectView("riderMy");
    }
}
