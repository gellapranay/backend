package com.antarmarg.util;

import com.antarmarg.entity.*;
import com.antarmarg.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final LocationNodeRepository nodeRepository;
    private final LocationEdgeRepository edgeRepository;
    private final DeliveryRepository deliveryRepository;
    private final ObstacleRepository obstacleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String superAdminPassword;

    public DataInitializer(
            UserRepository userRepository,
            FacilityRepository facilityRepository,
            LocationNodeRepository nodeRepository,
            LocationEdgeRepository edgeRepository,
            DeliveryRepository deliveryRepository,
            ObstacleRepository obstacleRepository,
            PasswordEncoder passwordEncoder,
            @Value("${SUPER_ADMIN_PASSWORD}") String superAdminPassword) {
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
        this.deliveryRepository = deliveryRepository;
        this.obstacleRepository = obstacleRepository;
        this.passwordEncoder = passwordEncoder;
        this.superAdminPassword = superAdminPassword;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // 1. Seed Users for All 4 Roles
            User superAdmin = new User();
            superAdmin.setName("System Administrator");
            superAdmin.setEmail("superadmin@antarmarg.com");
            superAdmin.setPhone("+91 9876543210");
            superAdmin.setPassword(passwordEncoder.encode(superAdminPassword));
            superAdmin.setRole(Role.SUPER_ADMIN);
            superAdmin.setIsApproved(true);
            superAdmin.setIsActive(true);
            userRepository.save(superAdmin);

            User customer = new User();
            customer.setName("Rahul Sharma");
            customer.setEmail("customer@antarmarg.com");
            customer.setPhone("+91 9849012345");
            customer.setPassword(passwordEncoder.encode("Customer@123"));
            customer.setRole(Role.CUSTOMER);
            customer.setIsApproved(true);
            customer.setIsActive(true);
            userRepository.save(customer);

            User deliveryPartner = new User();
            deliveryPartner.setName("Vikram Singh");
            deliveryPartner.setEmail("driver@antarmarg.com");
            deliveryPartner.setPhone("+91 9701234567");
            deliveryPartner.setPassword(passwordEncoder.encode("Driver@123"));
            deliveryPartner.setRole(Role.DELIVERY_PARTNER);
            deliveryPartner.setVehicleType("Motorbike");
            deliveryPartner.setVehicleNumber("TS-09-EA-4521");
            deliveryPartner.setServiceArea("Central Tech Zone & Gated Communities");
            deliveryPartner.setIsApproved(true);
            deliveryPartner.setIsActive(true);
            userRepository.save(deliveryPartner);

            User facilityAdmin = new User();
            facilityAdmin.setName("Priya Nair");
            facilityAdmin.setEmail("admin@greenvalley.com");
            facilityAdmin.setPhone("+91 9988776655");
            facilityAdmin.setPassword(passwordEncoder.encode("Facility@123"));
            facilityAdmin.setRole(Role.FACILITY_ADMIN);
            facilityAdmin.setOrganization("Green Valley Residents Association");
            facilityAdmin.setFacilityAccessRequest("Green Valley Apartments");
            facilityAdmin.setIsApproved(true);
            facilityAdmin.setIsActive(true);
            userRepository.save(facilityAdmin);
        }

        if (facilityRepository.count() > 0) {
            return;
        }

        User customer = userRepository.findByEmail("customer@antarmarg.com").orElse(null);
        User deliveryPartner = userRepository.findByEmail("driver@antarmarg.com").orElse(null);


        // 2. Seed Facility 1: Green Valley Apartments (Gated Community)
        Facility gv = new Facility();
        gv.setName("Green Valley Apartments");
        gv.setFacilityType(FacilityType.APARTMENT);
        gv.setAddress("Plot 42, Silicon Valley Avenue, Madhapur");
        gv.setCity("Hyderabad");
        gv.setState("Telangana");
        gv.setCountry("India");
        gv.setPostalCode("500081");
        gv.setLatitude(17.4485);
        gv.setLongitude(78.3752);
        gv.setBoundaryJson("[[17.4475,78.3740],[17.4495,78.3740],[17.4495,78.3765],[17.4475,78.3765]]");
        gv = facilityRepository.save(gv);

        // Green Valley Nodes
        LocationNode gvGateA = createNode(gv, null, "Gate A (Main Entrance)", NodeType.ENTRY_GATE, 0, 17.4476, 78.3742, 50.0, 450.0);
        LocationNode gvGateB = createNode(gv, null, "Gate B (Express Delivery Gate)", NodeType.ENTRY_GATE, 0, 17.4492, 78.3743, 50.0, 150.0);
        LocationNode gvRoadA = createNode(gv, null, "Central Boulevard (Internal Road)", NodeType.INTERNAL_ROAD, 0, 17.4482, 78.3748, 200.0, 300.0);
        LocationNode gvRoadB = createNode(gv, null, "North Access Way (Internal Road)", NodeType.INTERNAL_ROAD, 0, 17.4488, 78.3750, 200.0, 180.0);

        LocationNode gvTowerA = createNode(gv, null, "Tower A (Aster)", NodeType.TOWER, 0, 17.4479, 78.3755, 350.0, 400.0);
        LocationNode gvTowerB = createNode(gv, null, "Tower B (Bluebell)", NodeType.TOWER, 0, 17.4485, 78.3757, 350.0, 250.0);
        LocationNode gvTowerC = createNode(gv, null, "Tower C (Carnation)", NodeType.TOWER, 0, 17.4491, 78.3758, 350.0, 100.0);

        LocationNode gvLobbyB = createNode(gv, gvTowerB, "Tower B Ground Lobby", NodeType.ZONE, 0, 17.4485, 78.3757, 450.0, 250.0);
        LocationNode gvLift2 = createNode(gv, gvLobbyB, "Lift 2 (Tower B)", NodeType.LIFT, 0, 17.4485, 78.3758, 550.0, 240.0);
        LocationNode gvStairsB = createNode(gv, gvLobbyB, "Staircase B1 (Tower B)", NodeType.STAIRCASE, 0, 17.4485, 78.3758, 550.0, 270.0);

        LocationNode gvFloor12 = createNode(gv, gvTowerB, "Tower B - Floor 12", NodeType.FLOOR, 12, 17.4485, 78.3758, 700.0, 250.0);
        LocationNode gvCorridor12 = createNode(gv, gvFloor12, "Floor 12 West Corridor", NodeType.CORRIDOR, 12, 17.4485, 78.3759, 800.0, 250.0);
        LocationNode gvFlat1204 = createNode(gv, gvCorridor12, "Flat B-1204", NodeType.FLAT, 12, 17.4485, 78.3760, 920.0, 250.0);
        LocationNode gvFlat1201 = createNode(gv, gvCorridor12, "Flat B-1201", NodeType.FLAT, 12, 17.4485, 78.3760, 920.0, 180.0);

        // Green Valley Edges
        createEdge(gv, gvGateA, gvRoadA, 45.0, 30, true, true);
        createEdge(gv, gvGateB, gvRoadB, 35.0, 25, true, true);
        createEdge(gv, gvRoadA, gvRoadB, 60.0, 45, true, true);

        createEdge(gv, gvRoadA, gvTowerA, 30.0, 20, true, false);
        createEdge(gv, gvRoadA, gvTowerB, 40.0, 28, true, false);
        createEdge(gv, gvRoadB, gvTowerB, 25.0, 18, true, false); // Direct connection from Gate B / Road B to Tower B!
        createEdge(gv, gvRoadB, gvTowerC, 35.0, 22, true, false);

        createEdge(gv, gvTowerB, gvLobbyB, 15.0, 10, true, false);
        createEdge(gv, gvLobbyB, gvLift2, 10.0, 8, true, false); // Accessible Lift
        createEdge(gv, gvLobbyB, gvStairsB, 12.0, 10, false, false); // Inaccessible Stairs

        createEdge(gv, gvLift2, gvFloor12, 25.0, 35, true, false); // Lift up to Floor 12
        createEdge(gv, gvStairsB, gvFloor12, 45.0, 120, false, false); // Stairs up to Floor 12

        createEdge(gv, gvFloor12, gvCorridor12, 15.0, 12, true, false);
        createEdge(gv, gvCorridor12, gvFlat1204, 18.0, 15, true, false);
        createEdge(gv, gvCorridor12, gvFlat1201, 14.0, 12, true, false);

        // 3. Seed Facility 2: Anurag University (Campus)
        Facility univ = new Facility();
        univ.setName("Anurag University");
        univ.setFacilityType(FacilityType.UNIVERSITY);
        univ.setAddress("Venkatapur, Ghatkesar, Medchal-Malkajgiri");
        univ.setCity("Hyderabad");
        univ.setState("Telangana");
        univ.setCountry("India");
        univ.setPostalCode("500088");
        univ.setLatitude(17.4200);
        univ.setLongitude(78.6560);
        univ = facilityRepository.save(univ);

        LocationNode unMainGate = createNode(univ, null, "Main Gate 1", NodeType.ENTRY_GATE, 0, 17.4190, 78.6550, 50.0, 400.0);
        LocationNode unGate2 = createNode(univ, null, "Gate 2 (Engineering Gate)", NodeType.ENTRY_GATE, 0, 17.4210, 78.6555, 50.0, 150.0);
        LocationNode unGate3 = createNode(univ, null, "Gate 3 (Hostel & Sports Gate)", NodeType.ENTRY_GATE, 0, 17.4225, 78.6570, 50.0, 50.0);

        LocationNode unRoad1 = createNode(univ, null, "Engineering Way (Internal Road)", NodeType.INTERNAL_ROAD, 0, 17.4205, 78.6560, 200.0, 200.0);
        LocationNode unCseBlock = createNode(univ, null, "CSE Department Block", NodeType.BLOCK, 0, 17.4212, 78.6568, 350.0, 200.0);
        LocationNode unLibrary = createNode(univ, null, "Central Library Block", NodeType.BLOCK, 0, 17.4195, 78.6570, 350.0, 380.0);

        LocationNode unLift2 = createNode(univ, unCseBlock, "Lift 2 (CSE Block)", NodeType.LIFT, 0, 17.4212, 78.6569, 500.0, 200.0);
        LocationNode unFloor3 = createNode(univ, unCseBlock, "Floor 3 - AI & Data Science Lab", NodeType.FLOOR, 3, 17.4212, 78.6570, 650.0, 200.0);
        LocationNode unRoom305 = createNode(univ, unFloor3, "Lab Room 305", NodeType.ROOM, 3, 17.4213, 78.6571, 800.0, 200.0);

        createEdge(univ, unGate2, unRoad1, 30.0, 20, true, true);
        createEdge(univ, unMainGate, unLibrary, 50.0, 35, true, true);
        createEdge(univ, unLibrary, unRoad1, 60.0, 40, true, true);
        createEdge(univ, unRoad1, unCseBlock, 25.0, 18, true, false);
        createEdge(univ, unCseBlock, unLift2, 12.0, 8, true, false);
        createEdge(univ, unLift2, unFloor3, 20.0, 25, true, false);
        createEdge(univ, unFloor3, unRoom305, 15.0, 10, true, false);

        // 4. Seed Facility 3: City Care Hospital
        Facility hosp = new Facility();
        hosp.setName("City Care Hospital");
        hosp.setFacilityType(FacilityType.HOSPITAL);
        hosp.setAddress("Road No 1, Banjara Hills");
        hosp.setCity("Hyderabad");
        hosp.setState("Telangana");
        hosp.setCountry("India");
        hosp.setPostalCode("500034");
        hosp.setLatitude(17.4156);
        hosp.setLongitude(78.4482);
        hosp = facilityRepository.save(hosp);

        LocationNode hospMain = createNode(hosp, null, "Main Entrance Gate", NodeType.ENTRY_GATE, 0, 17.4150, 78.4475, 50.0, 300.0);
        LocationNode hospEmg = createNode(hosp, null, "Emergency Gate", NodeType.ENTRY_GATE, 0, 17.4155, 78.4478, 50.0, 100.0);
        LocationNode hospSvc = createNode(hosp, null, "Service Gate", NodeType.ENTRY_GATE, 0, 17.4160, 78.4485, 50.0, 500.0);
        LocationNode hospBlockB = createNode(hosp, null, "Super Specialty Block B", NodeType.BLOCK, 0, 17.4158, 78.4485, 300.0, 300.0);
        LocationNode hospLift3 = createNode(hosp, hospBlockB, "Lift 3 (Bed & Stretcher Lift)", NodeType.LIFT, 0, 17.4158, 78.4486, 500.0, 300.0);
        LocationNode hospCardio = createNode(hosp, hospBlockB, "Cardiology Ward (Floor 4)", NodeType.FLOOR, 4, 17.4158, 78.4487, 650.0, 300.0);
        LocationNode hospRoom412 = createNode(hosp, hospCardio, "Consultation Room 412", NodeType.ROOM, 4, 17.4159, 78.4488, 800.0, 300.0);

        createEdge(hosp, hospSvc, hospBlockB, 40.0, 25, true, true);
        createEdge(hosp, hospMain, hospBlockB, 60.0, 45, true, true);
        createEdge(hosp, hospEmg, hospBlockB, 50.0, 35, true, true);
        createEdge(hosp, hospBlockB, hospLift3, 15.0, 10, true, false);
        createEdge(hosp, hospLift3, hospCardio, 25.0, 30, true, false);
        createEdge(hosp, hospCardio, hospRoom412, 18.0, 12, true, false);

        // 5. Seed Facility 4: Tech Park One (Corporate Office)
        Facility tp = new Facility();
        tp.setName("Tech Park One");
        tp.setFacilityType(FacilityType.CORPORATE_OFFICE);
        tp.setAddress("HITEC City Phase 2");
        tp.setCity("Hyderabad");
        tp.setState("Telangana");
        tp.setCountry("India");
        tp.setPostalCode("500081");
        tp.setLatitude(17.4410);
        tp.setLongitude(78.3800);
        tp = facilityRepository.save(tp);

        LocationNode tpGate1 = createNode(tp, null, "North Security Gate 1", NodeType.ENTRY_GATE, 0, 17.4405, 78.3795, 50.0, 200.0);
        LocationNode tpGate2 = createNode(tp, null, "Express Visitor Gate 2", NodeType.ENTRY_GATE, 0, 17.4415, 78.3798, 50.0, 400.0);
        LocationNode tpBldgA = createNode(tp, null, "Building Alpha (Tower A)", NodeType.BUILDING, 0, 17.4412, 78.3805, 300.0, 300.0);
        LocationNode tpLift = createNode(tp, tpBldgA, "High-speed Lift Bank A", NodeType.LIFT, 0, 17.4412, 78.3806, 500.0, 300.0);
        LocationNode tpFloor8 = createNode(tp, tpBldgA, "Floor 8 - Engineering Hub", NodeType.FLOOR, 8, 17.4412, 78.3807, 650.0, 300.0);
        LocationNode tpOffice804 = createNode(tp, tpFloor8, "Suite 804 (DeepMind Research)", NodeType.OFFICE, 8, 17.4413, 78.3808, 800.0, 300.0);

        createEdge(tp, tpGate2, tpBldgA, 30.0, 20, true, true);
        createEdge(tp, tpGate1, tpBldgA, 55.0, 40, true, true);
        createEdge(tp, tpBldgA, tpLift, 12.0, 8, true, false);
        createEdge(tp, tpLift, tpFloor8, 30.0, 35, true, false);
        createEdge(tp, tpFloor8, tpOffice804, 15.0, 10, true, false);

        // 6. Seed Facility 5: MegaLog Warehouse
        Facility wh = new Facility();
        wh.setName("MegaLog Warehouse Hub");
        wh.setFacilityType(FacilityType.WAREHOUSE);
        wh.setAddress("Shamshabad Logistics Corridor");
        wh.setCity("Hyderabad");
        wh.setState("Telangana");
        wh.setCountry("India");
        wh.setPostalCode("501218");
        wh.setLatitude(17.2400);
        wh.setLongitude(78.4300);
        wh = facilityRepository.save(wh);

        LocationNode whTruckGate = createNode(wh, null, "Truck Entry Gate", NodeType.ENTRY_GATE, 0, 17.2395, 78.4290, 50.0, 250.0);
        LocationNode whLoading = createNode(wh, null, "Loading Dock 4", NodeType.LOADING_ZONE, 0, 17.2400, 78.4295, 200.0, 250.0);
        LocationNode whZoneC = createNode(wh, null, "Fulfillment Zone C", NodeType.ZONE, 0, 17.2405, 78.4302, 400.0, 250.0);
        LocationNode whAisle7 = createNode(wh, whZoneC, "Aisle 7 (High Density)", NodeType.AISLE, 0, 17.2406, 78.4304, 600.0, 250.0);
        LocationNode whRack14 = createNode(wh, whAisle7, "Rack 14", NodeType.RACK, 0, 17.2407, 78.4306, 750.0, 250.0);
        LocationNode whPos08 = createNode(wh, whRack14, "Position 08 (Tier 2)", NodeType.POSITION, 2, 17.2407, 78.4307, 900.0, 250.0);

        createEdge(wh, whTruckGate, whLoading, 50.0, 30, true, true);
        createEdge(wh, whLoading, whZoneC, 35.0, 20, true, true);
        createEdge(wh, whZoneC, whAisle7, 25.0, 15, true, false);
        createEdge(wh, whAisle7, whRack14, 15.0, 10, true, false);
        createEdge(wh, whRack14, whPos08, 8.0, 6, true, false);

        // 7. Seed Facility 6: Grand View Hotel
        Facility hotel = new Facility();
        hotel.setName("Grand View Hotel & Resort");
        hotel.setFacilityType(FacilityType.HOTEL);
        hotel.setAddress("Ocean Drive, Jubilee Hills");
        hotel.setCity("Hyderabad");
        hotel.setState("Telangana");
        hotel.setCountry("India");
        hotel.setPostalCode("500033");
        hotel.setLatitude(17.4350);
        hotel.setLongitude(78.4100);
        hotel = facilityRepository.save(hotel);

        LocationNode hGate = createNode(hotel, null, "Main Porte-Cochere Gate", NodeType.ENTRY_GATE, 0, 17.4345, 78.4095, 50.0, 300.0);
        LocationNode hTower = createNode(hotel, null, "Royal Wing Tower", NodeType.TOWER, 0, 17.4350, 78.4102, 300.0, 300.0);
        LocationNode hLift = createNode(hotel, hTower, "Guest Elevator 1", NodeType.LIFT, 0, 17.4351, 78.4103, 500.0, 300.0);
        LocationNode hFloor5 = createNode(hotel, hTower, "Floor 5 - Executive Suites", NodeType.FLOOR, 5, 17.4351, 78.4104, 650.0, 300.0);
        LocationNode hRoom502 = createNode(hotel, hFloor5, "Suite 502 (Presidential)", NodeType.ROOM, 5, 17.4352, 78.4105, 800.0, 300.0);

        createEdge(hotel, hGate, hTower, 40.0, 25, true, true);
        createEdge(hotel, hTower, hLift, 15.0, 10, true, false);
        createEdge(hotel, hLift, hFloor5, 25.0, 28, true, false);
        createEdge(hotel, hFloor5, hRoom502, 20.0, 15, true, false);

        // 8. Seed Facility 7: City Mall
        Facility mall = new Facility();
        mall.setName("City Center Mall");
        mall.setFacilityType(FacilityType.SHOPPING_MALL);
        mall.setAddress("Banjara Hills Main Road");
        mall.setCity("Hyderabad");
        mall.setState("Telangana");
        mall.setCountry("India");
        mall.setPostalCode("500034");
        mall.setLatitude(17.4220);
        mall.setLongitude(78.4490);
        mall = facilityRepository.save(mall);

        LocationNode mEntrance = createNode(mall, null, "Main Atrium Entrance", NodeType.ENTRY_GATE, 0, 17.4215, 78.4485, 50.0, 200.0);
        LocationNode mParking = createNode(mall, null, "Basement Parking B2 Entrance", NodeType.ENTRY_GATE, -2, 17.4225, 78.4488, 50.0, 450.0);
        LocationNode mFloor3 = createNode(mall, null, "Floor 3 - Fashion & Electronics", NodeType.FLOOR, 3, 17.4220, 78.4492, 400.0, 300.0);
        LocationNode mZoneC = createNode(mall, mFloor3, "Zone C (Tech Promenade)", NodeType.ZONE, 3, 17.4221, 78.4494, 600.0, 300.0);
        LocationNode mShop3C21 = createNode(mall, mZoneC, "Shop 3C-21 (Apple Store)", NodeType.SHOP, 3, 17.4222, 78.4496, 800.0, 300.0);

        createEdge(mall, mEntrance, mFloor3, 45.0, 35, true, false);
        createEdge(mall, mParking, mFloor3, 55.0, 42, true, false);
        createEdge(mall, mFloor3, mZoneC, 25.0, 18, true, false);
        createEdge(mall, mZoneC, mShop3C21, 15.0, 10, true, false);

        // 9. Seed Facility 8: Expo Center
        Facility expo = new Facility();
        expo.setName("Hitex Exhibition Center");
        expo.setFacilityType(FacilityType.EXHIBITION_CENTER);
        expo.setAddress("Izzat Nagar, Kothaguda");
        expo.setCity("Hyderabad");
        expo.setState("Telangana");
        expo.setCountry("India");
        expo.setPostalCode("500084");
        expo.setLatitude(17.4700);
        expo.setLongitude(78.3700);
        expo = facilityRepository.save(expo);

        LocationNode eMainGate = createNode(expo, null, "VIP Main Gate", NodeType.ENTRY_GATE, 0, 17.4695, 78.3690, 50.0, 200.0);
        LocationNode eHallA = createNode(expo, null, "Hall A (Grand Pavilion)", NodeType.BUILDING, 0, 17.4702, 78.3705, 350.0, 200.0);
        LocationNode eZone1 = createNode(expo, eHallA, "Zone 1 (Robotics & AI)", NodeType.ZONE, 0, 17.4704, 78.3708, 550.0, 200.0);
        LocationNode eBooth42 = createNode(expo, eZone1, "Booth 42 (Antarmarg Demo)", NodeType.POSITION, 0, 17.4705, 78.3710, 750.0, 200.0);

        createEdge(expo, eMainGate, eHallA, 60.0, 45, true, true);
        createEdge(expo, eHallA, eZone1, 30.0, 20, true, false);
        createEdge(expo, eZone1, eBooth42, 15.0, 10, true, false);

        // 10. Seed Realistic Active & Completed Deliveries
        Delivery del1 = new Delivery();
        del1.setTrackingNumber("LMI-7842-GV12");
        del1.setCustomer(customer);
        del1.setDeliveryPartner(deliveryPartner);
        del1.setFacility(gv);
        del1.setDestinationNode(gvFlat1204);
        del1.setStatus(DeliveryStatus.IN_PROGRESS);
        del1.setRecipientName("Rahul Sharma");
        del1.setRecipientPhone("+91 9849012345");
        del1.setDeliveryDescription("Priority Medical Essentials & Documents");
        del1.setLandmark("Near Tower B Express Lift");
        del1.setAccessInstructions("Buzz code #1204 at Tower B lobby");
        del1.setRecommendedGate(gvGateB);
        del1.setTotalDistanceMeters(145.0);
        del1.setEstimatedTimeSeconds(210);
        del1.setStartedAt(LocalDateTime.now().minusMinutes(8));
        deliveryRepository.save(del1);

        Delivery del2 = new Delivery();
        del2.setTrackingNumber("LMI-9104-AU05");
        del2.setCustomer(customer);
        del2.setDeliveryPartner(deliveryPartner);
        del2.setFacility(univ);
        del2.setDestinationNode(unRoom305);
        del2.setStatus(DeliveryStatus.COMPLETED);
        del2.setRecipientName("Prof. K. Venkatesh");
        del2.setRecipientPhone("+91 9849112233");
        del2.setDeliveryDescription("AI Lab FPGA Hardware Evaluation Board");
        del2.setRecommendedGate(unGate2);
        del2.setTotalDistanceMeters(182.0);
        del2.setEstimatedTimeSeconds(240);
        del2.setActualDurationSeconds(225);
        del2.setStartedAt(LocalDateTime.now().minusHours(2));
        del2.setCompletedAt(LocalDateTime.now().minusHours(2).plusMinutes(4));
        deliveryRepository.save(del2);

        // 11. Seed a sample active obstacle on Green Valley Road A to demonstrate dynamic rerouting to Gate B
        LocationEdge roadAToBldgB = edgeRepository.findBySourceNodeIdOrDestinationNodeId(gvRoadA.getId(), gvTowerB.getId()).stream().findFirst().orElse(null);
        if (roadAToBldgB != null) {
            Obstacle obs = new Obstacle();
            obs.setFacility(gv);
            obs.setLocationEdge(roadAToBldgB);
            obs.setObstacleType(ObstacleType.CONSTRUCTION);
            obs.setDescription("Paving stone maintenance in progress near Tower B south walkway");
            obs.setSeverity("HIGH");
            obs.setStatus("ACTIVE");
            obs.setReportedBy(deliveryPartner);
            obs.setExpiresAt(LocalDateTime.now().plusHours(3));
            obstacleRepository.save(obs);
        }
    }

    private LocationNode createNode(Facility facility, LocationNode parent, String name, NodeType type, int level, double lat, double lng, double x, double y) {
        LocationNode node = new LocationNode();
        node.setFacility(facility);
        node.setParentNode(parent);
        node.setName(name);
        node.setNodeType(type);
        node.setLevelNumber(level);
        node.setLatitude(lat);
        node.setLongitude(lng);
        node.setFloorPlanX(x);
        node.setFloorPlanY(y);
        node.setIsActive(true);
        return nodeRepository.save(node);
    }

    private LocationEdge createEdge(Facility facility, LocationNode src, LocationNode dst, double dist, int timeSec, boolean accessible, boolean vehicles) {
        LocationEdge edge = new LocationEdge();
        edge.setFacility(facility);
        edge.setSourceNode(src);
        edge.setDestinationNode(dst);
        edge.setDistanceMeters(dist);
        edge.setEstimatedTimeSeconds(timeSec);
        edge.setIsAccessible(accessible);
        edge.setAllowVehicles(vehicles);
        edge.setIsBidirectional(true);
        edge.setStatus("OPEN");
        edge.setIsActive(true);
        return edgeRepository.save(edge);
    }
}
