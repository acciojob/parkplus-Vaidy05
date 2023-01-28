package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        User user = userRepository3.findById(userId).get();
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

        if(user==null || parkingLot==null)
            throw new Exception("Cannot make reservation");

        List<Spot> spotList = parkingLot.getSpotList();

        SpotType spotType = SpotType.OTHERS;

        if(numberOfWheels<=2)
            spotType = SpotType.TWO_WHEELER;
        else if(numberOfWheels<=4)
            spotType = SpotType.FOUR_WHEELER;

        int minPrice = Integer.MAX_VALUE;
        Spot minPriceSpot = new Spot();

        if(spotList!=null){
            for(Spot spot : spotList){
                if(spot.getSpotType().equals(spotType) && minPrice>spot.getPricePerHour()){
                    minPriceSpot = spot;
                    minPrice = spot.getPricePerHour();
                }
            }
        }

        if(minPrice==Integer.MAX_VALUE)
            throw new Exception("Cannot make reservation");

        Reservation reservation = new Reservation(timeInHours,user,minPriceSpot);

        List<Reservation> reservationList = user.getReservationList();

        if(reservationList==null)
            reservationList = new ArrayList<>();

        reservationList.add(reservation);

        user.setReservationList(reservationList);

        List<Reservation> SpotReservationList = minPriceSpot.getReservationList();

        if(SpotReservationList==null)
            SpotReservationList = new ArrayList<>();

        SpotReservationList.add(reservation);

        userRepository3.save(user);
        spotRepository3.save(minPriceSpot);

        return reservation;
    }
}
