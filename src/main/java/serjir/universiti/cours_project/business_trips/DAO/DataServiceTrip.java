package serjir.universiti.cours_project.business_trips.DAO;


import serjir.universiti.cours_project.business_trips.entity.Trip;

import java.util.List;

public interface DataServiceTrip {

    public void createEntity(Trip trip);
    public Trip findTheEntity(Integer id);

    public void updateEntity (Integer id, Trip travel);

    public void deleteEntity(Integer id);

    public List<Trip> getTrips();

    public void deleteList(int id);

    public String createFile(Trip trip);
}
