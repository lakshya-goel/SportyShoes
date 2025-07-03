# 🏃‍♂️ Sporty Shoes — E-Commerce Website Prototype

Welcome to the **Sporty Shoes** e-commerce website prototype — built as a **Full Stack Developer Course-End Project**.

Sporty Shoes is a sports shoe manufacturer expanding from a walk-in store to an online sales portal: **sportyshoes.com**.

---

## 📌 **Project Objective**

Build a working prototype to:
- Demonstrate the **core features** of an online store
- Allow admin management of products, categories, users, and orders
- Provide secure login flows for admin and customers
- Generate purchase reports filtered by date and category
- Showcase Spring Boot, Spring MVC, Hibernate, Thymeleaf, and Spring Security

---

## ⚙️ **Tech Stack**

| Layer         | Technology           |
|---------------|----------------------|
| Backend       | Spring Boot, Spring MVC |
| ORM           | Hibernate JPA        |
| Security      | Spring Security, BCrypt |
| Database      | MySQL                |
| Frontend      | Thymeleaf, HTML5, CSS3, Bootstrap |
| Build Tool    | Maven                |
| Version Control | Git & GitHub       |

---

## 🗂️ **Key Features**

### ✅ **Admin Capabilities**
- **Admin Login & Logout**
- **Change Password**
- **Manage Products:** Add, update, delete products with category mapping.
- **Manage Categories:** Add and delete categories.
- **Manage Users:** View all users, search by username/email/name.
- **Manage Orders:** View orders, update order status.
- **Generate Purchase Reports:** Filter by date and category; view total revenue.

### 👤 **Customer Capabilities**
- **Register & Login**
- **Browse Products** by category
- **Search Products** by name/description
- **View Product Details**
- **Place Orders**
- **View My Orders**

---

## 🔐 **Security**

- Secure login for both Admin and Users.
- Session-based authentication.
- Passwords hashed with **BCrypt**.
- Access restrictions for `/admin/**` routes.

---

## 🗃️ **Database Design**

- **Admin**: `id`, `username`, `password`, `createdDate`, `lastLogin`
- **User**: `id`, `username`, `email`, `password`, `name`, `phone`, `address`, `registrationDate`, `role`
- **Product**: `id`, `name`, `description`, `price`, `stockQuantity`, `imageUrl`, `category`
- **Category**: `id`, `name`, `description`
- **Order**: `id`, `user`, `totalAmount`, `status`, `orderDate`
- **OrderItem**: `id`, `order`, `product`, `quantity`, `price`

Relationships:
- Product ➝ Category (`ManyToOne`)
- Order ➝ User (`ManyToOne`)
- Order ➝ OrderItems (`OneToMany`)
- OrderItem ➝ Product (`ManyToOne`)

---

## 📑 **Java Concepts Used**

- **Spring Boot Annotations:** `@Controller`, `@Service`, `@Repository`, `@Entity`
- **Dependency Injection:** `@Autowired`
- **JPA ORM:** Entity mapping, relationships, custom JPQL
- **Session Management:** Login state handling
- **Spring Security:** Authentication and role-based access
- **Thymeleaf:** Server-side rendering of dynamic views

---

## 📈 **Reports**

Admins can generate **purchase reports** with:
- Date range filter
- Category filter
- View total revenue

---

## 📦 **How To Run**

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/sportyshoes.git
