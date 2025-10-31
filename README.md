# Enterprise Document Management System

A Java Swing-based document management system with user authentication and document CRUD operations.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Terminal or Command Prompt

## How to Run

### Step 1: Compile the Project

Open a terminal in the project directory and run:

```bash
javac *.java
```

### Step 2: Run the Application

Start with the Login Screen:

```bash
java LoginFrame
```

Or directly open the Document Management System:

```bash
java DocumentManagementSystem
```

## Features

### Login System
- User registration
- User authentication
- Persistent user storage (users.txt)

### Document Management
- Upload documents with metadata (category, topic, tags)
- View documents with metadata display
- Edit documents and their metadata
- Delete documents
- Document list with filtering information

### Admin Features
- Delete users from the system

## Project Structure

- `LoginFrame.java` - Login and registration interface
- `DocumentManagementSystem.java` - Main application with document CRUD operations
- `User.java` - User authentication and management
- `Document.java` - Document data model
- `Category.java` - Category data model
- `Topic.java` - Topic data model
- `Tag.java` - Tag data model
- `users.txt` - User credentials storage
- `documents/` - Document storage folder

## Usage

1. Start the application using `java LoginFrame`
2. Create a new account or login with existing credentials
3. Upload documents using the Upload button
4. View, edit, or delete documents from the list
5. Use the menu bar for additional options

## Default Accounts

Check `users.txt` for existing accounts. Sample format:
```
username:password
```

## Notes

- Documents are stored in the `documents/` folder
- Metadata files (.meta) are created alongside each document
- The system creates necessary folders automatically
