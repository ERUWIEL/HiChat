package com.mycompany.hiChatJpa.view;

import com.mycompany.hiChatJpa.dto.LoginDTO;
import com.mycompany.hiChatJpa.dto.RecuperarContraseniaDTO;
import com.mycompany.hiChatJpa.dto.RegistroDTO;
import com.mycompany.hiChatJpa.dto.UsuarioPerfilDTO;
import com.mycompany.hiChatJpa.service.IChatService;
import com.mycompany.hiChatJpa.service.IUsuarioService;
import com.mycompany.hiChatJpa.service.impl.ChatService;
import com.mycompany.hiChatJpa.service.impl.UsuarioService;
import com.mycompany.hiChatJpa.view.pages.home.*;
import com.mycompany.hiChatJpa.view.pages.login.LoginPane;
import com.mycompany.hiChatJpa.view.pages.restorePassword.*;
import com.mycompany.hiChatJpa.view.pages.signin.*;
import java.awt.CardLayout;
import java.time.LocalDate;
import javax.swing.JPanel;

public class Controller {

    private final MainFrame mainFrame;
    private final JPanel contentPanel;
    private final CardLayout cardLayout;

    public static final String LOGIN_VIEW = "LOG_IN";
    public static final String SIGNIN_VIEW = "SIGN_IN";
    public static final String SIGNIN_DATE_VIEW = "SIGN_IN_DATE";
    public static final String SIGNIN_PASSWORD_VIEW = "SIGN_IN_PASSWORD";
    public static final String SIGNIN_BIO_VIEW = "SIGN_IN_BIO";
    public static final String SIGNIN_PICTURE_VIEW = "SIGN_IN_PICTURE";
    public static final String RSPSW_VIEW = "RESTORE_PASSWORD";
    public static final String RSPSW_CONFIRM_USER_VIEW = "RESTORE_PASSWORD_CONFIRMUSER";
    public static final String RSPSW_CHANGE_VIEW = "RESTORE_PASSWORD_CHANGE";

    public static final String HOME_VIEW = "HOME";

    // Instancias de pantallas
    private LoginPane loginPane;
    private SigninPane signinPane;
    private SigninDatePane signinDatePane;
    private SigninPasswordPane signinPasswordPane;
    private SigninBioPane signinBioPane;
    private SigninPicturePane signinPicturePane;
    private RestorePswUserPane restorePswPane;
    private RestorePswConfirmUserPane restorePswUserPane;
    private RestorePswChangePane restorePswChangePane;
    private HomePane homePane;

    //instancias de service
    private final IUsuarioService USUARIO_SERVICE = new UsuarioService();
    private final IChatService CHAT_SERVICE = new ChatService();

    // variables temporales DTO
    private RegistroDTO usuarioEnCreacion;
    private LoginDTO usuarioLogin;
    private UsuarioPerfilDTO usuarioPerfil;
    private RecuperarContraseniaDTO recuperarContrasenia;

    /**
     * metodo constructor de la clase
     *
     * @param mainFrame
     * @param contentPanel
     */
    public Controller(MainFrame mainFrame, JPanel contentPanel) {
        this.mainFrame = mainFrame;
        this.contentPanel = contentPanel;
        this.cardLayout = (CardLayout) contentPanel.getLayout();
        initializeScreens();
    }

    /**
     * metodo que inicializa las clases
     */
    private void initializeScreens() {
        loginPane = new LoginPane(this);
        signinPane = new SigninPane(this);
        signinDatePane = new SigninDatePane(this);
        signinPasswordPane = new SigninPasswordPane(this);
        signinBioPane = new SigninBioPane(this);
        signinPicturePane = new SigninPicturePane(this);
        restorePswPane = new RestorePswUserPane(this);
        restorePswUserPane = new RestorePswConfirmUserPane(this);
        restorePswChangePane = new RestorePswChangePane(this);

        // Agregar al contentPanel
        contentPanel.add(loginPane, LOGIN_VIEW);
        contentPanel.add(signinPane, SIGNIN_VIEW);
        contentPanel.add(signinDatePane, SIGNIN_DATE_VIEW);
        contentPanel.add(signinPasswordPane, SIGNIN_PASSWORD_VIEW);
        contentPanel.add(signinBioPane, SIGNIN_BIO_VIEW);
        contentPanel.add(signinPicturePane, SIGNIN_PICTURE_VIEW);
        contentPanel.add(restorePswPane, RSPSW_VIEW);
        contentPanel.add(restorePswUserPane, RSPSW_CONFIRM_USER_VIEW);
        contentPanel.add(restorePswChangePane, RSPSW_CHANGE_VIEW);
    }

    // Flujo controlado del login
    /**
     * metodo que permite mostrar el login limpio
     */
    public void showLogin() {
        limpiarDatosTemporales();
        limpiarLogin();
        usuarioLogin = new LoginDTO();
        cardLayout.show(contentPanel, LOGIN_VIEW);
    }

    /**
     * metodo que permite validar el login realizado
     *
     * @param correoElectronico
     * @param contrasenia
     */
    public void finalizarLogin(String correoElectronico, String contrasenia) {
        usuarioLogin.setCorreoElectronico(correoElectronico);
        usuarioLogin.setContrasena(contrasenia);
        verificarUsuario(usuarioLogin);
    }

    // Flujo controlado del Sigin
    /**
     * metodo que permite visualizar y controlar el signin
     */
    public void showSignin() {
        usuarioEnCreacion = new RegistroDTO();
        cardLayout.show(contentPanel, SIGNIN_VIEW);
    }

    /**
     * metodo que permite visualizar y controlar la vista de signin fecha
     *
     * @param nombre
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param writteInfo
     */
    public void signinAvanzarAFechaNacimiento(String nombre, String apellidoPaterno, String apellidoMaterno, boolean writteInfo) {
        if (writteInfo) {
            usuarioEnCreacion.setNombre(nombre);
            usuarioEnCreacion.setApellidoPaterno(apellidoPaterno);
            usuarioEnCreacion.setApellidoMaterno(apellidoMaterno);
        }

        cardLayout.show(contentPanel, SIGNIN_DATE_VIEW);
    }

    /**
     * metodo que permite visualizar y controlar la vista de signin password
     *
     * @param fechaNacimiento
     * @param correoElectronico
     * @param writteInfo
     */
    public void signinAvanzarAPassword(LocalDate fechaNacimiento, String correoElectronico, boolean writteInfo) {
        if (writteInfo) {
            usuarioEnCreacion.setFechaNacimiento(fechaNacimiento);
            usuarioEnCreacion.setCorreoElectronico(correoElectronico);
        }

        cardLayout.show(contentPanel, SIGNIN_PASSWORD_VIEW);
    }

    /**
     * metodo que permite visualizar y controlar la vista de signin biografia
     *
     * @param password
     * @param writteInfo
     */
    public void signinAvanzarABiografia(String password, boolean writteInfo) {
        if (writteInfo) {
            usuarioEnCreacion.setContrasena(password);
        }

        cardLayout.show(contentPanel, SIGNIN_BIO_VIEW);
    }

    /**
     * metodo que permite visualizar y controlar la vista de signin foto
     *
     * @param biografia
     * @param carrera
     * @param writteInfo
     */
    public void signinAvanzarAFotoPerfil(String biografia, String carrera, boolean writteInfo) {
        if (writteInfo) {
            usuarioEnCreacion.setBiografia(biografia);
            usuarioEnCreacion.setCarrera(carrera);
        }
        cardLayout.show(contentPanel, SIGNIN_PICTURE_VIEW);
    }

    /**
     * metodo que permite finalizar el proceso del signin
     *
     * @param rutaFotoTemporal
     */
    public void finalizarSignin(String rutaFotoTemporal) {
        usuarioEnCreacion.setRutaFotoTemporal(rutaFotoTemporal);
        guardarUsuario(usuarioEnCreacion);
        showLogin();
    }

    // Flujo controlado del restorePsd
    public void showRestorePassword() {
        recuperarContrasenia = new RecuperarContraseniaDTO();
        cardLayout.show(contentPanel, RSPSW_VIEW);
    }

    public void rstpAvanzarAConfirmarUsuario(String email, boolean writteInfo) {
        if (writteInfo) {
            recuperarContrasenia.setCorreoElectronico(email);
        }
        cardLayout.show(contentPanel, RSPSW_CONFIRM_USER_VIEW);
    }

    public void rstpAvanzarACambiarPassword(String nombre, String apellido) {
        recuperarContrasenia.setNombre(nombre);
        recuperarContrasenia.setApellido(apellido);
        cardLayout.show(contentPanel, RSPSW_CHANGE_VIEW);
    }

    public void finalizarRestorePassword(String contraseniaNueva) {
        recuperarContrasenia.setNuevaConstrasenia(contraseniaNueva);
        cambiarContrasenia(recuperarContrasenia);
        showLogin();
    }

    /**
     * metodo que llama al service para guradar al usuario
     *
     * @param usuario
     */
    private void guardarUsuario(RegistroDTO usuario) {
        USUARIO_SERVICE.registrarUsuario(usuario);
        System.out.println("usuario agregado " + usuario.getNombre());
    }

    /**
     * metodo que llama la service para loggear al usuario
     *
     * @param usuario
     */
    private void verificarUsuario(LoginDTO usuario) {
        try {
            usuarioPerfil = USUARIO_SERVICE.iniciarSesion(usuario);
            System.out.println("Usuario loggeado: " + usuarioPerfil.getNombre());
            if (homePane != null) {
                contentPanel.remove(homePane);
            }
            homePane = new HomePane(this, usuarioPerfil);
            contentPanel.add(homePane, HOME_VIEW);
            contentPanel.revalidate();
            contentPanel.repaint();

            cardLayout.show(contentPanel, HOME_VIEW);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(mainFrame, "Error al iniciar sesión: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarContrasenia(RecuperarContraseniaDTO recuperarConstrasenia) {         
        UsuarioPerfilDTO usuario = USUARIO_SERVICE.buscarPorCorreo(recuperarConstrasenia.getCorreoElectronico()); 
        USUARIO_SERVICE.reestablecerContrasenia(usuario.getIdUsuario(), recuperarContrasenia.getNuevaConstrasenia());
    }
    
    // metodos de utileria
    
    
    private void limpiarDatosTemporales() {
        usuarioEnCreacion = null;
        usuarioLogin = null;
        usuarioPerfil = null;
    }

    /**
     * metodo que permite cerrar la sesion actual y limpia los datos
     */
    public void cerrarSesion() {
        if (homePane != null) {
            contentPanel.remove(homePane);
            homePane = null;
        }
        limpiarDatosTemporales();
        contentPanel.revalidate();
        contentPanel.repaint();

        showLogin();
    }

    private void limpiarLogin() {
        if (loginPane != null) {
            contentPanel.remove(loginPane);
            loginPane = new LoginPane(this);
            contentPanel.add(loginPane, LOGIN_VIEW);
        }
        
        if (signinPane != null) {
            contentPanel.remove(signinPane);
            contentPanel.remove(signinDatePane);
            contentPanel.remove(signinPasswordPane);
            contentPanel.remove(signinBioPane);
            contentPanel.remove(signinPicturePane);
            
            signinPane = new SigninPane(this);
            signinDatePane = new SigninDatePane(this);
            signinPasswordPane = new SigninPasswordPane(this);
            signinBioPane = new SigninBioPane(this);
            signinPicturePane = new SigninPicturePane(this);
            
            contentPanel.add(signinPane, SIGNIN_VIEW);
            contentPanel.add(signinDatePane, SIGNIN_DATE_VIEW);
            contentPanel.add(signinPasswordPane, SIGNIN_PASSWORD_VIEW);
            contentPanel.add(signinBioPane, SIGNIN_BIO_VIEW);
            contentPanel.add(signinPicturePane, SIGNIN_PICTURE_VIEW);
        }
        
        if (restorePswPane != null){
            contentPanel.remove(restorePswPane);
            contentPanel.remove(restorePswUserPane);
            contentPanel.remove(restorePswChangePane);
            
            restorePswPane = new RestorePswUserPane(this);
            restorePswUserPane = new RestorePswConfirmUserPane(this);
            restorePswChangePane = new RestorePswChangePane(this);
            
            contentPanel.add(restorePswPane, RSPSW_VIEW);
            contentPanel.add(restorePswUserPane, RSPSW_CONFIRM_USER_VIEW);
            contentPanel.add(restorePswChangePane, RSPSW_CHANGE_VIEW);
        }
        
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

}
