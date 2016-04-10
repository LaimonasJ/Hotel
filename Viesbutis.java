package viesbutis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import logic.Hotel;
import logic.Room;
import logic.Client;
import logic.employees.Chef;
import logic.employees.Employee;
import logic.employees.Manager;
import logic.employees.Receptionist;


/**
 * This is sample program, which allows user to view hotels, their information
 * and make appointments for rooms.
 * @author Laimonas Juras
 */
public class Viesbutis
{
    
    public static Client me = new Client("Laimonas",
                                         "Juras", 
                                         LocalDate.now(),
                                         LocalDate.now().plus(7, ChronoUnit.DAYS));
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        List<Hotel> hotels = new ArrayList<>();
        initHotelList(hotels);
        System.out.println("Welcome to hotel manager!");
        int option = 1;
        while (option != 0)
        {
            System.out.println("");
            System.out.println("Available options:");
            System.out.println("0. Exit");
            System.out.println("1. View hotel list");
            System.out.println("2. Choose hotel");
            System.out.println("Enter your option:");
            try
            {
                option = Integer.parseInt(new Scanner(System.in).nextLine());
                switch(option)
                {
                    case 0:
                        System.out.println("Bye!");
                        break;
                    case 1:
                        System.out.println("");
                        viewHotelList(hotels);
                        break;
                    case 2:
                        choseHotel(hotels);
                        break;
                    default:
                        printOptionError();
                        break;
                }
            }
            catch (Exception e)
            {
                printOptionError();
            }
        }
    }
    
    public static void initHotelList(List<Hotel> hotels)
    {

        Client client = new Client("Kitas", "Klientas", LocalDate.now(), LocalDate.now());
        
        for (int i = 1; i < 4; ++i)
        {
            hotels.add(new Hotel("Hotel"+Integer.toString(i)));
        }
        for (Hotel hotel: hotels)
        {
            hotel.addEmployee(new Chef(hotel.getName() + "_Chef", "Cheferson"));
            hotel.addEmployee(new Manager(hotel.getName() + "_Manager", "Manegerson"));
            hotel.addEmployee(new Receptionist(hotel.getName() + "_Recep", "Recepson"));
            hotel.addRoom(new Room(4, Room.Type.PREMIUM));
            hotel.addRoom(new Room(1, Room.Type.STANDARD));
            hotel.addRoom(new Room(1, Room.Type.SUITE));
        }
        
        for (Room room: hotels.get(1).getRooms())
        {
            room.setClient(client);
        }
        
        hotels.get(0).addRoom(new Room(2, Room.Type.STANDARD));
        for(Room room: hotels.get(1).getRooms())
        {
            //room.
        }
    }

    private static void viewHotelList(List<Hotel> hotels)
    {
        System.out.println("");
        for(Hotel hotel: hotels)
        {
            System.out.println(hotel.toString());
        }
    }

    private static void choseHotel(List<Hotel> hotels)
    {
        int option = 1;
        while (option != 0)
        {        
            System.out.println("");
            System.out.println("0. Back");
            System.out.println("Available hotels:");
            for(int i = 0; i < hotels.size(); ++i)
            {
                System.out.println(Integer.toString(i + 1) + ". " + hotels.get(i).toString());
            }
            System.out.println("Enter hotel number:");
            try
            {
                option = Integer.parseInt(new Scanner(System.in).nextLine());
                if (option == 0)
                {
                    return;
                }
                if (isBetween(option, 1, hotels.size()))
                {
                    manageHotel((hotels.get(option - 1)));
                }
                else
                {
                    printOptionError();
                }
            }
            catch (Exception e)
            {
                printOptionError();
            }
        }
    }
    
    
    private static void printOptionError()
    {
        System.out.println("Not a valid option!");
    }
    
    public static boolean isBetween(int x, int lower, int upper)
    {
        return lower <= x && x <= upper;
    }

    private static void manageHotel(Hotel hotel)
    {
        int option = 1;
        while (option != 0)
        {
            System.out.println("");
            System.out.println(hotel.toString() + " options:");
            System.out.println("0. Back");
            System.out.println("1. View Employee list");
            System.out.println("2. View all rooms");
            System.out.println("3. Book a Room");
            System.out.println("4. Renounce book request");
            System.out.println("Enter your option:");
            try
            {
                option = Integer.parseInt(new Scanner(System.in).nextLine());
                switch(option)
                {
                    case 0:
                        break;
                    case 1:
                        System.out.println("");
                        viewEmployeeList(hotel.getEmployees());
                        break;
                    case 2:
                        viewAllRooms(hotel.getRooms());
                        break;
                    case 3:
                        bookRoom(me, hotel);
                        break;
                    case 4:
                        renounceRequest(hotel);
                        break;
                    default:
                        printOptionError();
                        break;
                }
            }
            catch (Exception e)
            {
                printOptionError();
            }
        }
    }

    private static void viewEmployeeList(List<Employee> employees)
    {
        for(Employee employee: employees)
        {
            System.out.println(employee.getName() + " " +
                               employee.getSurname() + " - " +
                               employee.getPosition());
        }
    }

    private static void viewAllRooms(List<Room> rooms)
    {
        for(Room room: rooms)
        {
            System.out.println(room.getType() + ", Booked: " +
                              //(room.isBooked(me.getArrival())?"yes":"no") +
                              ((room.getClient() == null)?"no":room.getBookedFromTo()) +
                              ", Beds: " + room.getBedrooms());
        }
    }


    private static void renounceRequest(Hotel hotel)
    {
        if (!hotel.getClients().contains(me))
        {
            System.out.println("");
            System.out.println("You don't have reservation in this hotel!");
            return;
        }
        for(Room room: hotel.getRooms())
        {
            if (room.getClient() == me)
            {
                room.setClient(null);
                hotel.removeClient(me);
                System.out.println("");
                System.out.println("Book request renounced!");
                return;
            }  
        }
        System.out.println("");
        System.out.println("You don't have room booked in this hotel");
    }

    private static Room chooseRoom (Hotel hotel)
    {
        List<Room> availableRooms = new ArrayList<>();
        for(Room room: hotel.getRooms())
        {
            if (room.getClient() == null)
            {
                availableRooms.add(room);
            }
        }
        int option = 1;
        while (option != 0)
        {        
            System.out.println("");
            System.out.println("0. Back");
            System.out.println("Available Rooms:");
            for(int i = 0; i < availableRooms.size(); ++i)
            {
                System.out.println(Integer.toString(i + 1) + ". " + 
                                   availableRooms.get(i).getType() + ", Beds: " +
                                   availableRooms.get(i).getBedrooms());
            }
            System.out.println("Choose Room to book:");
            try
            {
                option = Integer.parseInt(new Scanner(System.in).nextLine());
                if (isBetween(option, 1, availableRooms.size()))
                {
                    return availableRooms.get(option - 1);
                }
                else
                {
                    printOptionError();
                }
            }
            catch (Exception e)
            {
                printOptionError();
            }
        }
        return null;
    }

    private static void bookRoom(Client client, Hotel hotel)
    {
        if(hotel.getClients().contains(me))
        {
            System.out.println("");
            System.out.println("You're already booked in this hotel!");
        }
        else
        {
            Room room = chooseRoom(hotel);
            if (room == null)
                return;
            try
            {
                setReservationPeriod(me);
            }
            catch(Exception e)
            {
                return;
            }
            System.out.println("");
            System.out.println("Insert paying operations here...");
            System.out.println("");
            hotel.addClient(me);
            room.setClient(me);
            System.out.println("Reservation made!");
        }
    }

    private static void setReservationPeriod(Client me)
    {
        LocalDate arrival;
        int daysToStay;
        try
        {
            arrival = getArrivalDate();
            daysToStay = getDaysToStay();
        }
        catch(Exception e)
        {
            throw e;
        }
        
        me.setArrival(arrival);
        me.setDeparture(arrival.plusDays(daysToStay));
        
    }


    private static int getDaysToStay() throws IllegalStateException
    {
        while(true)
        {
            System.out.println("");
            System.out.println("Enter amount of days you wish to Stay:");
            try
            {
                int daysToStay = Integer.parseInt(new Scanner(System.in).nextLine());
                if (isBetween(daysToStay, 0, 31))
                {
                    return daysToStay;
                }
                else
                {
                    System.out.println("Maximum reservation lenght is 31 days!");
                }
            }
            catch(NumberFormatException e)
            {
                System.out.println("Not a valid input!");
            }
        }
    }

    private static LocalDate getArrivalDate() throws IllegalStateException
    {
        String input;
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false);
        while(true)
        {
            System.out.println("");
            System.out.println("Enter date of your arrival (yyyy-MM-dd)");
            System.out.println("or 0 to go back:");
        
            try
            {
                input = new Scanner(System.in).nextLine();
                if (input.equals("0"))
                     throw new IllegalStateException();
                date = formatter.parse(input);
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (localDate.isEqual(LocalDate.now()) || (localDate.isAfter(LocalDate.now()) && localDate.isBefore(LocalDate.now().plusYears(1))))
                    return localDate;
                else
                {
                    System.out.println("");
                    System.out.println("You can only make reservations from " +
                                       LocalDate.now().toString() + " to " +
                                       LocalDate.now().plusYears(1).toString() +
                                       "!");
                }
            }
            catch (ParseException e)
            {
                System.out.println("");
                System.out.println("Incorrect input, make sure your input");
                System.out.println("follows date format: (yyyy-MM-dd)");
            }           
        }
    }
    
}

