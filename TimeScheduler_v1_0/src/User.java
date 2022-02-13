package src;

/**
 * <p>
 * This class is used to create a {@code User} object.
 * </p>
 * <p>
 * The user object contains the following:
 * </p>
 * <ul>
 * <li>A unique ID</li>
 * <li>An username</li>
 * <li>A display name of the user(optional)</li>
 * <li>An email address</li>
 * <li>A phone number (optional)</li>
 * </ul>
 * 
 * @author Tam Thai Hoang
 */
public class User {
    /**
     * The unique ID of the user.
     */
    private String id;

    /**
     * The unique username of the user.
     */
    private String username;

    /**
     * The display name of the user.
     */
    private String name;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The phone number of the user.
     */
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
