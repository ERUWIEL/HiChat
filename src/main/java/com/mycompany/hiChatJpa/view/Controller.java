package com.mycompany.hiChatJpa.view;

import com.mycompany.hiChatJpa.dto.RegistroDTO;
import com.mycompany.hiChatJpa.service.IChatService;
import com.mycompany.hiChatJpa.service.IUsuarioService;
import com.mycompany.hiChatJpa.service.impl.ChatService;
import com.mycompany.hiChatJpa.service.impl.UsuarioService;

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
    
    
    //instancias de service
    private IUsuarioService usuarioService = new UsuarioService();
    private IChatService chatService = new ChatService();

    // variables temporales DTO
    private RegistroDTO usuarioEnCreacion;

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
    public void showLogin() {
        limpiarDatosTemporales();
        cardLayout.show(contentPanel, LOGIN_VIEW);
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
     * @param password
     * @param writteInfo 
     */
    public void signinAvanzarABiografia(String password, boolean writteInfo) {
        if(writteInfo){
            usuarioEnCreacion.setContrasena(password);
        }
        
        cardLayout.show(contentPanel, SIGNIN_BIO_VIEW);
    }

    /**
     * metodo que permite visualizar y controlar la vista de signin foto
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

    public void finalizarSignin(String rutaFotoTemporal) {
        usuarioEnCreacion.setRutaFotoTemporal(rutaFotoTemporal);
        guardarUsuario(usuarioEnCreacion);
        showLogin();
    }

    
    
    
    // Flujo controlado del restorePsd
    public void showRestorePassword() {
        cardLayout.show(contentPanel, RSPSW_VIEW);
    }

    public void avanzarAConfirmarUsuario(String email) {
        // Lógica para enviar código
        //restorePswUserPane.setEmail(email);
        cardLayout.show(contentPanel, RSPSW_CONFIRM_USER_VIEW);
    }

    public void avanzarACambiarPassword(String codigo) {
        // Validar código
        //restorePswChangePane.setCodigo(codigo);
        cardLayout.show(contentPanel, RSPSW_CHANGE_VIEW);
    }

    // metodos de utileria y persistencia
    private void limpiarDatosTemporales() {
        usuarioEnCreacion = null;
    }

    private void guardarUsuario(RegistroDTO usuario) {
        usuarioService.registrarUsuario(usuario);
        System.out.println("usuario agregado " + usuario.getNombre());
    }

    public RegistroDTO getUsuarioEnCreacion() {
        return usuarioEnCreacion;
    }
}
