package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot parkingLot = new ParkingLot(name,address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        SpotType spotType = SpotType.OTHERS;

        if(numberOfWheels<=2)
            spotType = SpotType.TWO_WHEELER;
        else if(numberOfWheels<=4)
            spotType = SpotType.FOUR_WHEELER;

        Spot spot = new Spot(spotType,pricePerHour,false);
        spot.setParkingLot(parkingLot);

        List<Spot> spotList = new ArrayList<>();

        if(parkingLot.getSpotList()!=null)
            spotList = parkingLot.getSpotList();

        spotList.add(spot);

        parkingLot.setSpotList(spotList);

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
            spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

        ParkingLot parkingLot = new ParkingLot();
        Spot spot = new Spot();

        if(parkingLotRepository1.existsById(parkingLotId) && spotRepository1.existsById(spotId)) {
            parkingLot = parkingLotRepository1.findById(parkingLotId).get();
            spot = spotRepository1.findById(spotId).get();
        }


        List<Spot> spotList = new ArrayList<>();

        if(parkingLot.getSpotList()!=null) {
            spotList = parkingLot.getSpotList();

            for (Spot spot1 : spotList) {
                if (spot1.equals(spot)){
                    spot1.setPricePerHour(pricePerHour);
                    spot1.setParkingLot(parkingLot);
                    spot=spot1;
                }

            }
        }

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
