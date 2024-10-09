package com.example.api_rest_git;

import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityInterface extends AppCompatActivity {

    private EditText inputText;  // Usar EditText para capturar el texto del usuario
    private TextView display;
    private Button button_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_interface);

        // Referencias a los componentes
        inputText = findViewById(R.id.InputText); // EditText para capturar el nombre de usuario
        display = findViewById(R.id.display);
        button_show = findViewById(R.id.button_show);

        // Establecer un OnClickListener para el botón
        button_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el nombre de usuario del EditText
                String usuario = inputText.getText().toString().trim();  // Eliminar espacios innecesarios

                // Si el usuario no está vacío, proceder con la petición
                if (!usuario.isEmpty()) {
                    API_rest_Class(usuario);
                } else {
                    display.setText("Por favor, introduce un nombre de usuario.");
                }
            }
        });
    }

    // Interfaz para la API de GitHub
    public interface GitHubService {
        @GET("users/{user}/repos")
        Call<List<Repo>> listRepos(@Path("user") String user);
    }

    // Modelo para los repositorios
    public static class Repo {
        public String name;
        public int id;
    }

    // Método para hacer la llamada a la API de GitHub
    public void API_rest_Class(String usuario) {
        // Cambiar el texto mientras se realiza la petición
        display.setText("Realizando petición...");

        // Crear un nuevo hilo para realizar la llamada a la API
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Utilizamos el builder implícito en Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Creamos una instancia de la API de GitHub
                GitHubService service = retrofit.create(GitHubService.class);
                Call<List<Repo>> repos = service.listRepos(usuario);

                try {
                    Response<List<Repo>> response = repos.execute();

                    // Actualizamos la UI desde el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.isSuccessful()) {
                                List<Repo> repoList = response.body();
                                if (repoList != null) {
                                    StringBuilder reposInfo = new StringBuilder();
                                    for (Repo repo : repoList) {
                                        reposInfo.append("Nombre del repositorio: ").append(repo.name)
                                                .append(" | ID del repositorio: ").append(repo.id)
                                                .append("\n");
                                    }
                                    display.setText(reposInfo.toString());
                                } else {
                                    display.setText("No existen repositorios del usuario");
                                }
                            } else {
                                display.setText("Ha fallado la petición con status " + response.code());
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();

                    // Mostrar el error en la UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            display.setText("Error al realizar la petición: " + e.getMessage());
                        }
                    });
                }
            }
        }).start();
    }
}
