package gm.zona_fit;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

//@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {

	@Autowired
	private IClienteServicio clienteServicio;

	private static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);

	String nl = System.lineSeparator();

	public static void main(String[] args) {
		logger.info("\n--- Iniciando la aplicacion ---");
		// Levantar la fabrica de spring
		SpringApplication.run(ZonaFitApplication.class, args);
		logger.info("\n--- Aplicacion finalizada! ---");
	}

	@Override
	public void run(String... args) throws Exception {
		zonaFitApp();
	}

	public void zonaFitApp(){
		logger.info(nl + nl + "*** Aplicacion Zona Fit (GYM) ***" + nl);

		var salir = false;
		var consola = new Scanner(System.in);

		while (!salir){
			try {
				var opcion = mostarMenu(consola);
				salir = ejecutarOpciones(opcion, consola);

			}catch (Exception e){
				logger.info(nl + "Error al ejecutar opciones: " + e.getMessage());
			}
			finally {
				logger.info(nl);
			}
		}
	}

	private int mostarMenu(Scanner consola){
		logger.info("""
                Menu:
                1. Listar
                2. Buscar Cliente
                3. Agregar
                4. Modificar
                5. Eliminar
                6. Salir
                
                Respuesta:\s""");
		// Leemos y retornamos la opcion seleccionada
		return Integer.parseInt(consola.nextLine());
	}

	private boolean ejecutarOpciones(int opcion, Scanner consola){
		var salir= false;
		switch (opcion){
			case 1 -> listarClientes();
			case 2 -> buscarCliente(consola);
			case 3 -> agregarCliente(consola);
			case 4 -> modificarCliente(consola);
			case 5 -> eliminarCliente(consola);
			case 6 -> {
				logger.info("Hasta pronto!" + nl + nl);
				salir = true;
			}
			default -> logger.info("Opcion no reconocida: " + opcion);
		}
		return salir;
	}

	private void listarClientes(){
		logger.info(nl + "--- Listar Cliente ---" + nl);
		var clietes = clienteServicio.listarClientes();
		clietes.forEach(cliente -> logger.info(cliente.toString() + nl));
	}

	private void buscarCliente(Scanner consola){
		logger.info(nl + "--- Buscar Cliente ---");
		logger.info(nl + "Id Cliente: ");
		var idCliente = Integer.parseInt(consola.nextLine());
		var cliente = clienteServicio.buscarCliente(idCliente);
		if (cliente != null)
			logger.info("Cliente encontrado: " + cliente + nl);
		else
			logger.info("Cliente NO encontrado: " + cliente + nl);
	}

	private void agregarCliente(Scanner consola){
		logger.info(nl + "--- Agregar Cliente ---" + nl);
		// Solicitar datos del nuevo cliente
		logger.info("Nombre: ");
		var nombre = consola.nextLine();
		logger.info("Apellido: ");
		var apellido = consola.nextLine();
		logger.info("Membresia: ");
		var membresia = Integer.parseInt(consola.nextLine());
		// Se crea el Cliente
		var cliente = new Cliente();
		// Setear los valores
		cliente.setNombre(nombre);
		cliente.setApellido(apellido);
		cliente.setMembresia(membresia);

		// Agregamos a la BD
		clienteServicio.guardarCliente(cliente);

		logger.info("Cliente agregado: " + cliente + nl);
	}

	private void modificarCliente(Scanner consola){
		logger.info(nl + "--- Modificar Cliente ---" +  nl);
		logger.info("Id Cliente: ");
		var idCliente = Integer.parseInt(consola.nextLine());
		var cliente = clienteServicio.buscarCliente(idCliente);
		if (cliente != null){
			logger.info("Nombre: ");
			var nombre = consola.nextLine();
			logger.info("Apellido: ");
			var apellido = consola.nextLine();
			logger.info("Membresia: ");
			var membresia = Integer.parseInt(consola.nextLine());

			//Seteamos los valores al cliente encontrado
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setMembresia(membresia);
			clienteServicio.guardarCliente(cliente);
			logger.info("Cliente modficado: " + cliente + nl);
		}
		else
			logger.info("Cliente NO encontrado: " + cliente);
	}

	private void eliminarCliente(Scanner consola){
		logger.info(nl + "--- Eliminar Cliente---" + nl);
		logger.info("Id Cliente: ");
		var idCliente = Integer.parseInt(consola.nextLine());
		var cliente = clienteServicio.buscarCliente(idCliente);

		if (cliente!= null){
			clienteServicio.eliminarCliente(cliente);
			logger.info("Cliente eliminado: " + cliente + nl);
		}
		else
			logger.info("Cliente NO encontrado: " + cliente + nl);
	}
}
