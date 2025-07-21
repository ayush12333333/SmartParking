## ✅ Features

### 🔐 Authentication & Authorization
- User registration and login with **JWT authentication**
- Role-based access: **Admin** and **User**
- Secure API access for authorized users only

### 👤 User Management
- Users can **view and update their profile** (e.g., name, phone number, vehicle type)
- Passwords stored securely using **BCrypt hashing**
- JWT token-based session management

### 🅿️ Parking Slot Management
- **Admin** can:
    - Create new parking slots with: `slotId`, `slotNumber`, `location`, `vehicleType` (car/bike), `fareAmount`
    - Monitor bookings (planned future enhancement)
- **User** can:
    - View available slots (filtered by `location` and `vehicleType`)
    - Book or cancel a parking slot
    - Check-in and check-out with live time tracking
    - Fare automatically calculated based on reserved hours and vehicle type
    - **Late checkout penalty** applied

### 💵 Fare & Transaction Handling
- ₹20/hour for **bike**, ₹40/hour for **car**
- Fare is calculated based on:
    - Booking duration
    - Actual check-in and check-out time
    - Extra charges applied for **late checkout**
- Checkout operations are wrapped in **transactional service methods** to ensure:
    - Consistent updates to `booking`, `slot`, and `user` data
    - Prevention of race conditions or partial updates
- Database operations (booking, slot status updates, fare calculation) are atomic using `@Transactional` in service layer

---

## 🧠 Advanced Design Highlights

- Clean architecture with separation of concerns (Controller, Service, Repository)
- Error handling with meaningful HTTP status codes
- Secure and RESTful design principles
- Modular code for future integration with ride booking (Uber/Ola-like)

---


---

## 🚀 Getting Started

To get started with this project, ensure you have **Java**, **MySQL**, and **Maven** installed on your system.

