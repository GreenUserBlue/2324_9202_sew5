from PIL import Image, ImageDraw, ImageFont

# Define the text blocks and their hierarchy
blocks = {
    "Planung": ["OSP", "PSP", "Planung abgeschlossen"],
    "Durchführung": ["Programmieren", "Testen", "Abnahme", "Alles Fertig"]
}

# Create an image with white background
img = Image.new('RGB', (1048, 223), color = (255, 255, 255))
d = ImageDraw.Draw(img)

# Set the font size
try:
    # Trying to use a standard font
    font = ImageFont.truetype("arial.ttf", 16)
except IOError:
    # If standard font is not available, PIL will use a default one
    font = ImageFont.load_default()

# Define starting positions
x = 10
y_top = 10
y_bottom = img.height / 2 + 10

# Define box sizes
box_height = 20
box_width = 150

# Draw the text boxes
for key in blocks.keys():
    d.rectangle([x, y_top, x + box_width, y_top + box_height], outline=(0, 0, 0))
    d.text((x+5, y_top+5), key, fill=(0, 0, 0), font=font)
    y_offset = y_top + box_height + 5
    for item in blocks[key]:
        d.rectangle([x, y_offset, x + box_width, y_offset + box_height], outline=(0, 0, 0))
        d.text((x+5, y_offset+5), item, fill=(0, 0, 0), font=font)
        y_offset += box_height + 5
    x += box_width + 10

# Save the image
file_path = 'example1_pillow.png'
img.save(file_path)