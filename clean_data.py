import pandas as pd
import numpy as np
import datetime as dt

INPUT_FILE = r'C:\Users\unkin\Downloads\games.csv'
OUTPUT_FILE = r'C:\ProyectosPersonales\GameVault-api\games_ready_for_db.csv'
OUT_GAMES = 'gamesDB.csv'
OUT_GENRES = 'genresDB.csv'
OUT_RELATION = 'game_genresDB.csv'

# 1 CARGA DE DATOS
try:
    #"encoding='utf-8'" quiere decir que es la libreria universal de caracteres
    #"on_bad_lines='skip' quiere decir que salta lineas rotas si el archivo viene mal" 
    df = pd.read_csv(INPUT_FILE, encoding='utf-8', on_bad_lines='skip', index_col=False)
    total_filas = len(df)
except FileNotFoundError:
    print(f"No se encontro el archivo {INPUT_FILE}")
    exit()

# 2 SELECCION DE COLUMNAS
columnas_necesarias = {
    'AppID' : 'gameID',
    'Name' : 'title',
    'Genres' : 'genre',
    'Price' : 'price',
    'Release date' : 'releaseYear'
}

''' Verifico que existan las columnas antes de renombrarlas
set() sirve para comparar conjuntos de nombres '''
columnas_en_csv = set(df.columns)
columnas_que_quiero = set(columnas_necesarias.keys())

if not columnas_que_quiero.issubset(columnas_en_csv):
    print("Las columnas en el csv no coinciden con las que busco")
    print(f"El csv tiene {list(df.columns)}")
    exit()

# Filtro lo que quiero y renombro
df = df[columnas_necesarias.keys()]
df = df.rename(columns=columnas_necesarias)

# 3 LIMPIEZA
# Limpiar release date para solo tener el año en el que salio el juego
print("Analizando fechas (Extrayendo solo el año)...")

#convierto todo a str para poder buscar
df['releaseYear'] = df['releaseYear'].astype(str)

# Uso REGEX (Expresiones Regulares)
#r'(19\d{2}|20\d{2})' significa: Busca algo que empiece con 19 o 20 y tenga 2 números más
#Esto captura "2021" de "Oct 21, 2021" y también de "2021" solo.
df['releaseYear'] = df['releaseYear'].str.extract(r'(19\d{2}|20\d{2})')[0]

year_actual = dt.date.today().year

#Filtro de años validos
df = df.dropna(subset=['releaseYear'])
#Convierto a entero
df['releaseYear'] = df['releaseYear'].astype(int)
df = df[ (df['releaseYear'] >= 1970) & (df['releaseYear'] <= year_actual + 2) ]

# 4 FILTRADO FINAL
print("Borrando filas incompletas (Sin titulo o genero)...")
df = df.dropna(subset=['title', 'genre', 'gameID'])

# Convierto año a entero
df['releaseYear'] = df['releaseYear'].astype(int)

# 5 GENERACION DE TABLAS NORMALIZADAS
print("Preparando archivos csv...")
# A) Tabla GAMES
games_export = df[['gameID', 'title', 'price', 'releaseYear']].copy()
games_export = games_export.drop_duplicates(subset=['gameID'])

# B) Preparacion de generos
# Convertir "Action", "Indie" a lista ["Action", "Indie"]
df['genre_list'] = df['genre'].astype(str).str.split(',')
# Crear una fila por cada genero
df_exploded = df.explode('genre_list')[['gameID', 'genre_list']]
df_exploded['genre_list'] = df_exploded['genre_list'].str.strip()
# Borrar vacios
df_exploded = df_exploded[df_exploded['genre_list'] != '']

# C) Tabla GENRES
unique_genres = df_exploded['genre_list'].unique()
genres_db = pd.DataFrame(unique_genres, columns=['name'])
# Asigno un id unico a cada genero
genres_db['genreID'] = range(1, len(genres_db) + 1)
genres_db = genres_db[['genreID', 'name']]

# D) Tabla INTERMEDIA (GAME_GENRES)
relation_db = df_exploded.merge(genres_db, left_on='genre_list', right_on='name')

# Me interesan las llaves foraneas (IDs)
relation_db = relation_db[['gameID', 'genreID']]

# 6 EXPORTACION
print("Guardando archivos limpios...")
df.to_csv(OUTPUT_FILE, index=False, encoding='utf-8')
games_export.to_csv(OUT_GAMES, index=False, encoding='utf-8')
genres_db.to_csv(OUT_GENRES, index=False, encoding='utf-8')
relation_db.to_csv(OUT_RELATION, index=False, encoding='utf-8')

print("\n Proceso terminado")
print(f"Juegos originales: {total_filas}")
print(f"Juegos validos exportados: {len(df)}")
print(f"Archivo generado: {OUTPUT_FILE}")
print(f"games.csv: {len(games_export)} juegos")
print(f"genres.csv: {len(genres_db)} generos unicas")
print(f"game_genres.csv: {len(relation_db)} relaciones creadas")