# Finance Account

Spring Boot 3 migration of the legacy Struts 2 / JSP finance application. The UI keeps the
original screens and routes (including the old `.action` URLs), while the backend runs on
Spring MVC, Thymeleaf, `JdbcTemplate` and MySQL 8.

## Stack

- Java 21, Spring Boot 3.3.5 (Web MVC, Thymeleaf, JDBC, Validation, Actuator)
- MySQL 8
- OpenPDF for the monthly PDF export
- Maven build, multi-stage Docker image, Docker Compose for MySQL or the full stack

## Quick start: jar with Docker MySQL

Start the database first. Compose publishes MySQL on `localhost:3307` so it can coexist with a
MySQL installed on your machine at 3306.

```bash
docker compose up -d
```

Build and run the jar. Its defaults already match this database (`localhost:3307`, `root`/`root`):

```bash
mvn clean package
java -jar target/finance-account-2.0.0.jar
```

Open <http://localhost:8080> and log in with:

- **Username:** `admin`
- **Password:** `admin123`

If port 8080 is already taken, run with `SERVER_PORT=8082` or stop the process using it. To use a
different database, set the `DB_*` environment variables described below.

## Full containerized deployment

```bash
docker compose --profile app up --build -d
```

This starts both the application and MySQL. Open <http://localhost:8080> (override with
`APP_PORT`). The app container connects to the `db` service on the internal MySQL port 3306; only
the database is published on the host, at port 3307.

Stop the local jar first if it is using port 8080.

The `db` service runs `sql/01_schema.sql` and `sql/02_seed.sql` from `sql/` automatically the
first time the MySQL data volume is created. Both scripts are idempotent, so they can also be
applied manually to an existing database:

```bash
mysql -u root -p < sql/01_schema.sql
mysql -u root -p < sql/02_seed.sql
```

Useful commands:

```bash
docker compose logs -f db                          # database logs
docker compose --profile app logs -f app           # application container logs
docker compose --profile app down                  # stop everything, keep database data
docker compose down -v                             # stop and delete the database volume (destructive)
docker compose exec db mysqldump -uroot -proot sample > backup.sql
```

To change the default ports or database password, create a `.env` file next to
`docker-compose.yml`:

```dotenv
MYSQL_ROOT_PASSWORD=change-me
MYSQL_HOST_PORT=3307
APP_PORT=8080
```

The `.env` file is read by Docker Compose only. A locally run jar does not load it, so export the
same values when they differ from the defaults:

```bash
DB_PORT=3307 DB_PASSWORD=change-me java -jar target/finance-account-2.0.0.jar
```

> The SQL scripts in `sql/` only run when the MySQL data directory is empty. For an existing
> volume, apply `sql/01_schema.sql` and `sql/02_seed.sql` manually or run `docker compose down -v`
> before starting again (this deletes all data). Changing `MYSQL_ROOT_PASSWORD` after the volume
> has been created does not change the password stored in MySQL.
>
> The Compose file uses the MySQL `root` account so an existing `sample` database can be reused
> as-is. For production, change `MYSQL_ROOT_PASSWORD`, stop publishing MySQL, and create a
> dedicated database user.

## Troubleshooting connectivity

- **`Access denied for user 'root'@'localhost'`** while running the jar: the jar defaults to the
  Docker database on `localhost:3307`. Check it is running and healthy with `docker compose ps`.
  If you prefer your own MySQL on 3306, export `DB_PORT=3306 DB_PASSWORD=<your-mysql-password>`.
- **Login with `admin` / `admin123` fails on an existing volume**: the volume was created before
  the seed was corrected, so reset the password hash:
  ```bash
  docker compose exec -T db mysql -uroot -proot \
    -e "UPDATE sample.user SET password='GhrmCpG5ZtPNHY7DgwofvQ==' WHERE user_name='admin';"
  ```
- **Port already in use**: stop the old jar or IDE run before starting the jar, and use
  `APP_PORT=8081 docker compose --profile app up -d` if port 8080 is occupied by something else.

## Local development with an existing MySQL

Prerequisites: JDK 21, Maven 3.9+, MySQL 8. The application defaults to the Docker database on
port 3307, so point it at your local server explicitly:

```bash
mysql -u root -p < sql/01_schema.sql
mysql -u root -p < sql/02_seed.sql
export DB_PORT=3306 DB_USER=root DB_PASSWORD=<your-mysql-password>
mvn spring-boot:run
```

Or build the jar and run it:

```bash
mvn clean package
DB_PORT=3306 DB_PASSWORD=<your-mysql-password> java -jar target/finance-account-2.0.0.jar
```

## Configuration

| Variable | Default | Description |
| --- | --- | --- |
| `SERVER_PORT` | `8080` | HTTP port of the application |
| `DB_HOST` | `localhost` | MySQL host |
| `DB_PORT` | `3307` | MySQL host port; use `3306` for a locally installed MySQL |
| `DB_NAME` | `sample` | Database name |
| `DB_USER` | `root` | Database user |
| `DB_PASSWORD` | `root` | Database password |
| `THYMELEAF_CACHE` | `true` | Set to `false` while editing templates |

Actuator exposes `GET /actuator/health` and `GET /actuator/info` without authentication.

## Routes

| Area | Routes |
| --- | --- |
| Login | `/`, `/index`, `/userLogin`, `/logout`, `/failAction` |
| Users | `/userDetails`, `/userUpdate`, `/userController`, `/userRegistration`, `/regSuccess` |
| Dashboard | `/successAction`, `/successActionGet`, `/dailySheet`, `/getCustomerDailySheet`, `/customerDetails` |
| Customers | `/customerTab`, `/closingCustomerAction`, `/customerDetailsUpdate[Close]`, `/customerDetailsUpdates[Close]`, `/customerDetailsDelete[Close]` |
| Installments | `/instalOpenAction`, `/instalCloseAction`, `/installDetails[Close]`, `/displayInstall[Close]`, `/updateInstallDetails[Close]`, `/deleteInstallDetails[Close]` |
| Deposits | `/depositsAction`, `/displayDepositDetails`, `/searchDeposits`, `/depositDetailsSave`, `/depositDetailsUpdate`, `/depositDetailsDelete`, `/depositMonthPayment`, `/ajaxAction`, `/displayMonthlyDetails`, `/displayPaymentInterestDetails`, `/updateMonthPayment`, `/deleteMonthPayment` |
| Extra transactions | `/extraTransaction`, `/searchExtraDetails`, `/displayExtraDetails`, `/extraTransactionSave`, `/extraTransactionUpdate`, `/extraTransactionDelete` |
| Reports | `/report`, `/reportDailySheet`, `/reportDeposits`, `/reportMonthlyInterest`, `/reportExtraTransaction`, `/monthlyReportDetails`, `/exportMonthlydetails` |
| Mobile numbers | `/mobileNumber`, `/mobileNumberAdd` |

Legacy URLs ending in `.action` (for example `/customerTab.action`) are still accepted.

## Project layout

```
src/main/java/com/laxmifinance
├── config        Spring configuration, login interceptor, .action suffix filter
├── controller    MVC controllers
├── form          request binding objects
├── model         view/domain models
├── repository    JdbcTemplate repositories
├── service       PDF export and monthly net-amount scheduler
└── util          crypto, date and browser helpers
src/main/resources
├── templates     Thymeleaf views (fragments, customer, deposit, installment, report)
├── static        CSS, JS and images
└── application.yml
sql               01_schema.sql, 02_seed.sql
Dockerfile        multi-stage build (Maven → JRE 21 Alpine, non-root)
docker-compose.yml  MySQL 8 with automatic schema initialization; app service under the "app" profile
```

## Migration notes and security

- SQL access is fully parameterized; no request values are concatenated into statements.
- Passwords keep the legacy `PBEWithMD5AndDES` ciphertext format so existing users can log in
  unchanged (`admin` / `admin123` is seeded).
- Registration and password change require an authenticated session, and users may only change
  their own password.
- The monthly net-amount calculation runs on a Spring `@Scheduled` job instead of the legacy
  background thread.
- Session timeout is unlimited, matching the legacy behaviour; run behind HTTPS and restrict
  network access in production.
