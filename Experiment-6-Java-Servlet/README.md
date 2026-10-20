# Experiment 6 – Java Servlet (Jakarta Servlet)

## Aim
To study server-side programming with Java Servlets and implement a web application covering HTTP,
the Servlet API, the Servlet life cycle, configuration and context, request/response objects,
session handling, Servlet events and filters.

## Requirements
| Software | Version |
|----------|---------|
| Java JDK | 17 or later |
| Apache Maven | 3.x |
| Apache Tomcat | 11 (uses the `jakarta.servlet` package) |

## Project Structure
```
Experiment-6-Java-Servlet
├── pom.xml
└── src/main
    ├── java/com/servlet
    │   ├── HelloServlet.java      – doGet() / doPost(), reads form parameter
    │   ├── LifeCycleServlet.java  – init(), doGet(), destroy()
    │   ├── LoginServlet.java      – validates login, creates HttpSession
    │   ├── HomeServlet.java       – protected page, reads session attribute
    │   ├── LogoutServlet.java     – session.invalidate()
    │   ├── AuthFilter.java        – blocks /home unless logged in
    │   ├── MyFilter.java          – logs every request URI
    │   ├── AppListener.java       – ServletContextListener
    │   └── SessionListener.java   – HttpSessionListener
    └── webapp
        ├── index.html             – Student Login form
        └── hello.html             – GET and POST forms
```

## URL Mappings
| URL | Component | Concept |
|-----|-----------|---------|
| `/hello` | HelloServlet | First Servlet, GET & POST, `getParameter()` |
| `/lifecycle` | LifeCycleServlet | Servlet life cycle |
| `/login` | LoginServlet | POST, HttpSession |
| `/home` | HomeServlet + AuthFilter | Session + authentication filter |
| `/logout` | LogoutServlet | `session.invalidate()` |
| `/*` | MyFilter | Simple logging filter |

## Build and Deploy
1. Build the WAR file:
   ```bash
   mvn package
   ```
2. Copy `target/servlet.war` into `Tomcat/webapps/`.
3. Start Tomcat (`bin/startup.bat` on Windows, `bin/startup.sh` on Linux/macOS).
4. Open http://localhost:8080/servlet/

## Integrated Practical – Login + Session + Filter
Login with **username `admin`** and **password `1234`**.
```
index.html --POST--> LoginServlet --valid--> create HttpSession --> /home
                                                                     |
                                                                AuthFilter
                                                             session valid?
                                                              /          \
                                                           YES            NO
                                                     HomeServlet    redirect to index.html
```

## Output
- `/hello` → **Hello Students!** / *Welcome to Servlet Programming*
- `/hello?name=Harsh` → **Welcome Harsh**
- Login with `admin` / `1234` → **Login Successful** → *Go to Home* → **Student Home – Welcome admin**
- Opening `/home` without logging in → redirected to the login page
- Tomcat console:
  ```
  Application Started
  Request intercepted: /servlet/login
  Session Created
  Response completed
  ```
