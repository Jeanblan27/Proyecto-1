package Presentation.Model.Funcionario;

public class Funcionario {

    private String nombre;
    private String id;
    private String clave;
    private String telefono;
    private Rol rol;


    public Funcionario() {}
    public Funcionario(String id, String nombre, String clave,String telefono,Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.telefono = telefono;
        this.rol = rol;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Rol getRol() { return rol; }
}
