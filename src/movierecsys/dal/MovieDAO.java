/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import movierecsys.be.Movie;

/**
 *
 * @author pgn
 */
public class MovieDAO
{

    private static final String MOVIE_SOURCE = "data/movie_titles.txt";

    /**
     * Gets a list of all movies in the persistence storage.
     *
     * @return List of movies.
     * @throws java.io.FileNotFoundException
     */
    public List<Movie> getAllMovies() throws FileNotFoundException, IOException
    {
        List<Movie> allMovies = new ArrayList<>();
        File file = new File(MOVIE_SOURCE);

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                try
                {
                    Movie mov = stringArrayToMovie(line);
                    allMovies.add(mov);
                } catch (Exception ex)
                {
                    //Do nothing we simply do not accept malformed lines of data.
                    //In a perfect world you should at least log the incident.
                }
            }
        }
        return allMovies;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t
     * @return
     * @throws NumberFormatException
     */
    private Movie stringArrayToMovie(String t)
    {
        String[] arrMovie = t.split(",");

        int id = Integer.parseInt(arrMovie[0]);
        int year = Integer.parseInt(arrMovie[1]);
        String title = arrMovie[2];

        Movie mov = new Movie(id, year, title);
        return mov;
    }

    /**
     * Creates a movie in the persistence storage.
     *
     * @param releaseYear The release year of the movie
     * @param title The title of the movie
     * @return The object representation of the movie added to the persistence
     * storage.
     */
    private Movie createMovie(int releaseYear, String title)
    {
        List<Movie> allMovies = new ArrayList<>();
        try {
            allMovies = getAllMovies();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //int theBiggestID=0;
        int theBiggestID = allMovies.get(0).getId();

        for(Movie movie: allMovies)
        {
            if(movie.getId()>= theBiggestID)
                theBiggestID = movie.getId();
        }

        Movie mov = new Movie(theBiggestID, releaseYear, title);
        return mov;
    }


    /**
     * Deletes a movie from the persistence storage.
     *
     * @param movie The movie to delete.
     */
    private void deleteMovie(Movie movie)
    {
       File inputFile = new File(MOVIE_SOURCE);
       File temporaryFile = new File("temporaryFile.txt");
    BufferedReader reader = null;
    BufferedWriter writer=null;
        try {
             reader = new BufferedReader(new FileReader(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
             writer = new BufferedWriter(new FileWriter(temporaryFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String lineToRemove = String.valueOf(movie.getId());
        String currentLine="";

        while(true) {
            try {
                if (!((currentLine = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            if(trimmedLine.contains(lineToRemove)) continue;
            try {
                writer.write(currentLine + System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean successful = temporaryFile.renameTo(inputFile);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Updates the movie in the persistence storage to reflect the values in the
     * given Movie object.
     *
     * @param movie The updated movie.
     */
    private void updateMovie(Movie movie)
    {
        FileWriter fw = null;
        try {
            fw = new FileWriter(MOVIE_SOURCE, true);
            fw.append(movie.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a the movie with the given ID.
     *
     * @param id ID of the movie.
     * @return A Movie object.
     */
    private Movie getMovie(int id)
    {
        List<Movie> allMovies = new ArrayList<>();
        try {
            allMovies = getAllMovies();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Movie movie: allMovies)
        {
            if(movie.getId()==id)
                return movie;
        }
        //TODO Get one Movie
        return null;
    }


}
