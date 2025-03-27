# Application Setup and Usage Guide

## Prerequisites

Before getting started, you'll need to install Docker and Docker Compose on your operating system.

### Installing Docker

#### Windows
1. Download Docker Desktop for Windows from the official Docker website
2. Double-click the installer to run the installation
3. Follow the installation wizard, ensuring "Use WSL 2 instead of Hyper-V" is selected if you're using Windows Subsystem for Linux
4. Restart your computer when prompted
5. Launch Docker Desktop from the Start menu
6. Verify installation by opening Command Prompt and running:
   ```
   docker --version
   docker-compose --version
   ```

#### macOS
1. Download Docker Desktop for Mac from the official Docker website
2. Open the .dmg file and drag the Docker icon to the Applications folder
3. Launch Docker from the Applications folder
4. Accept the user agreement and allow system permissions
5. Verify installation by opening Terminal and running:
   ```
   docker --version
   docker-compose --version
   ```

#### Linux (Ubuntu/Debian)
1. Update package index:
   ```bash
   sudo apt-get update
   ```
2. Install required packages:
   ```bash
   sudo apt-get install apt-transport-https ca-certificates curl software-properties-common
   ```
3. Add Docker's official GPG key:
   ```bash
   curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
   ```
4. Set up the Docker repository:
   ```bash
   sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
   ```
5. Install Docker CE:
   ```bash
   sudo apt-get update
   sudo apt-get install docker-ce
   ```
6. Install Docker Compose:
   ```bash
   sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
   sudo chmod +x /usr/local/bin/docker-compose
   ```
7. Verify installation:
   ```bash
   docker --version
   docker-compose --version
   ```

## Application Setup

### Cloning the Repository
```bash
git clone https://github.com/konarjg/SWIFTCodeAPI.git
cd SWIFTCodeAPI
```

### Running the Application
```bash
# Build and start the containers
docker-compose up --build

# To run in detached mode
docker-compose up -d --build
```

## API Testing

### Using React Client
1. The React client will be accessible at `http://localhost:3000`
2. Navigate to the client in your web browser
3. Use sidebar to test various endpoints
4. For POST and DELETE requests provide the API secret key: **3a4988f022d8ad9a0f72d4ed6933a818a57ed3b3b8bdeec86162b98cfcf046f4**

### Using Postman

#### Installing Postman

##### Windows
1. Download the Postman installer from the official Postman website
2. Run the downloaded executable
3. Follow the installation wizard
4. Launch Postman after installation

##### macOS
1. Download the Postman macOS version
2. Open the .dmg file
3. Drag Postman to the Applications folder
4. Launch Postman from Applications

##### Linux
1. Download the Postman Linux AppImage
2. Make the file executable:
   ```bash
   chmod +x Postman-*.AppImage
   ```
3. Run the AppImage directly or move it to a convenient location

### API Authentication and Testing

#### Obtaining Authentication Token
1. Create a POST request to `http://localhost:8080/auth/token`
2. Set the request body to JSON with the following structure:
   ```json
   {
     "sub": "konarjg",
     "key": "3a4988f022d8ad9a0f72d4ed6933a818a57ed3b3b8bdeec86162b98cfcf046f4",
     "scope": ["USER", "ADMIN"]
   }
   ```
   - `sub`: Any username (e.g., "konarjg")
   - `scope`: 
     - Use `["USER", "ADMIN"]` for POST and DELETE requests
     - Use `["USER"]` for GET requests
   - `key`: Must be exactly the value shown above for POST and DELETE requests

#### Using the Authentication Token
1. After sending the token request, you'll receive a bearer token
2. For subsequent requests, add the token to the Authorization header:
   ```
   Authorization: Bearer [YOUR_GENERATED_TOKEN]
   ```
3. **Important**: The token expires after 1 minute

### Example Postman Request Workflow
1. POST to `http://localhost:8080/auth/token`
   - Body: JSON with `sub`, `key`, and `scope`
2. Copy the returned bearer token
3. Create your next request (GET/POST/DELETE)
4. Add the bearer token to the Authorization header
5. Send the request

## Stopping the Application
```bash
# Stop and remove containers
docker-compose down

# Remove volumes (if needed)
docker-compose down -v
```

## Troubleshooting
- Ensure Docker is running before executing docker-compose commands
- Check container logs with `docker-compose logs [service-name]`
- Verify network ports are not being used by other applications

## Additional Notes
- Always use the correct scope and key for authentication
- The token is short-lived (1 minute), so you may need to regenerate frequently
- React app handles authentication automatically
