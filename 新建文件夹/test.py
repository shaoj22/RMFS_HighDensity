import random
import random

def generate_map(rows, cols, num_workstations):
    # 初始化地图
    map_data = [['1' for _ in range(cols)] for _ in range(rows)]

    # 放置通道、货架和工作站
    for col in range(0, cols, 3):
        for row in range(rows):
            map_data[row][col] = '1'

        if col + 1 < cols:
            for row in range(rows):
                if row == 0 or row == rows - 1 or row == rows -2 :
                    continue
                else:
                    map_data[row][col + 1] = '8'

        if col + 2 < cols:
            for row in range(rows):
                if row == 0 or row == rows - 1 or row == rows -2 :
                    continue
                else:
                    map_data[row][col + 2] = '8'
    # for col in range(0, cols):
    #     for row in range(rows):
    #         if row == rows - 2 or row == rows - 3:
    #             map_data[row][col] = '1'

    # 放置工作站
    workstations_placed = 0
    col = 3
    while workstations_placed < num_workstations:
        map_data[rows-1][col] = '2'  # 2表示工作站
        workstations_placed += 1
        col += 3

    # 生成地图文件内容
    map_content = f"{rows} {cols}\n" + '\n'
    for row in map_data:
        map_content += ' '.join(row) + '\n'

    # 生成1111矩阵
    matrix_1111 = [['1111' for _ in range(cols)] for _ in range(rows)]
    
    for row in range(rows):
        for col in range(cols):
            if row == 0:
                matrix_1111[row][col] = '0111'
                if col == 0:
                    matrix_1111[row][col] = '0101'
                if col == cols - 1:
                    matrix_1111[row][col] = '1001'
                
            elif row == rows - 1:
                matrix_1111[row][col] = '1011'
                if col == 0:
                    matrix_1111[row][col] = '1001'
                if col == cols - 1:
                    matrix_1111[row][col] = '1001'
                
            elif col == 0:
                matrix_1111[row][col] = '1101'
                if row == 0:
                    matrix_1111[row][col] = '0101'
                if row == rows - 1:
                    matrix_1111[row][col] = '1001'
                
            elif col == cols - 1:
                matrix_1111[row][col] = '1110'
                if row == 0:
                    matrix_1111[row][col] = '0110'
                if row == rows - 1:
                    matrix_1111[row][col] = '1010'
    matrix_1111[0][0] = '0101'
    matrix_1111[0][cols - 1] = '0110'
    matrix_1111[rows - 1][0] = '1001'
    matrix_1111[rows - 1][cols - 1] = '1010'
    
    # 生成新的1111矩阵
    matrix_1111_content = ''
    for row in matrix_1111:
        matrix_1111_content += ' '.join(row) + '\n'
    
    
    combined_content = map_content + '\n' + matrix_1111_content
    
    return combined_content

def save_to_file(content, filename='generated_map.txt'):
    with open(filename, 'w') as file:
        file.write(content)

# 获取用户输入
rows = 23
cols = 91
workstations = 10

# 生成地图并保存到文件
# 生成地图并保存到文件
map_content = generate_map(rows, cols, workstations)
save_to_file(map_content)

print(f"已生成地图文件: generated_map.txt")