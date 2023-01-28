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

        User user = new User();
        ParkingLot parkingLot = new ParkingLot();

        if(parkingLotRepository3.existsById(parkingLotId) && userRepository3.existsById(userId)) {
            user = userRepository3.findById(userId).get();
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }
        else
            throw new NullPointerException();

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
            throw new NullPointerException();

        Reservation reservation = new Reservation(timeInHours,user,minPriceSpot);

        List<Reservation> reservationList = new ArrayList<>();

        if(user.getReservationList()!=null)
            reservationList = user.getReservationList();

        reservationList.add(reservation);

        user.setReservationList(reservationList);

        List<Reservation> SpotReservationList = new ArrayList<>();

        if(minPriceSpot.getReservationList()!=null)
            SpotReservationList = minPriceSpot.getReservationList();

        SpotReservationList.add(reservation);

        userRepository3.save(user);
        spotRepository3.save(minPriceSpot);

        return reservation;
    }
}
