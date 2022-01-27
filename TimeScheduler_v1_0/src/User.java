package src;

/**
 * <p>
 * This class is used to create a {@link User} object.
 * <p>
 * <p>
 * The user object contains the following:
 * <p>
 * <p>
 * - A unique ID
 * <p>
 * <p>
 * - An username
 * <p>
 * <p>
 * - A name
 * <p>
 * <p>
 * - An email
 * <p>
 * <p>
 * - A phone number
 * <p>
 * 
 * @author Tam Thai Hoang 1370674
 */
public class User {
    private String id;
    private String username;
    private String name;
    private String email;
    private String phone;

    /**
     * Constructor for the {@link User} class for the {@code Database} to pass in
     * the ID.
     * 
     * @param id       The unique ID of the user
     * @param username The username of the user
     * @param name     The name of the user
     * @param email    The email of the user
     * @param phone    The phone number of the user
     */
    public User(String id, String username, String name, String email, String phone) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Constructor for the User class for the register with required information.
     * 
     * @param username The username of the user
     * @param name     The name of the user
     * @param email    The email of the user
     * @param phone    The phone number of the user
     */
    public User(String username, String name, String email, String phone) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Constructor for the User class in the register with no information.
     * 
     */
    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
