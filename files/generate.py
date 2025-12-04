import csv
import random
import string
from datetime import datetime, timedelta
import sys

def generate_optimized_csv(filename, num_rows):
    header = 'Название;Жанр;Участники;Синглы;Альбомы;Дата основания;Описание;Координаты.x;Координаты.y;Студия.Название;Студия.Адрес;Лучший альбом.Название;Лучший альбом.Длительность\n'
    
    genres = ('PROGRESSIVE_ROCK', 'POST_PUNK', 'BRIT_POP')
    letters = string.ascii_letters + ' '
    
    with open(filename, 'w', encoding='utf-8') as f:
        f.write(header)
        
        buffer = []
        buffer_size = 50000
        
        for i in range(1, num_rows + 1):
            title = ''.join(random.choices(letters, k=15)).title()
            
            genre = ''
            if random.random() > 0.1:
                genre = random.choice(genres)
            
            members = ''
            if random.random() > 0.1:
                members = str(random.randint(1, 8))
            
            singles = str(random.randint(1, 50))
            albums = str(random.randint(1, 20))
            
            # Быстрая генерация даты
            date = f"{random.randint(1960, 2023)}-{random.randint(1, 12):02d}-{random.randint(1, 28):02d}"
            
            description = ''
            if random.random() > 0.1:
                description = ''.join(random.choices(letters, k=50)).title()
            
            coord_x = f"{random.uniform(-180.0, 180.0):.6f}"
            coord_y = str(random.randint(-90, 90))
            
            studio_name = studio_addr = ''
            if random.random() > 0.15:
                studio_name = ''.join(random.choices(letters, k=12)).title()
                studio_addr = ''.join(random.choices(letters, k=30)).title()
            
            album_name = album_duration = ''
            if random.random() > 0.15:
                album_name = ''.join(random.choices(letters, k=15)).title()
                album_duration = str(random.randint(30, 180))
            
            # Собираем строку CSV
            row = f"{title};{genre};{members};{singles};{albums};{date};{description};{coord_x};{coord_y};{studio_name};{studio_addr};{album_name};{album_duration}\n"
            buffer.append(row)
            
            # Сбрасываем буфер
            if len(buffer) >= buffer_size:
                f.write(''.join(buffer))
                buffer.clear()
                
            # Прогресс
            if i % 100000 == 0:
                print(f"Сгенерировано {i} из {num_rows} строк...")
        
        # Записываем остаток
        if buffer:
            f.write(''.join(buffer))
    
    print(f"Файл '{filename}' успешно создан с {num_rows} строками.")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Использование: python script.py <файл.csv> <количество_строк>")
        sys.exit(1)
    
    generate_optimized_csv(sys.argv[1], int(sys.argv[2]))

