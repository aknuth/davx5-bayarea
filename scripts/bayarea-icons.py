#!/usr/bin/env python3
#
# Bay Area Sync: generates the launcher icons (app-ose/src/main/res) and the Google Play icon
# (app-ose/branding/play-icon-512.png) from app-ose/branding/icon.png (transparent background).
#
# Requires Pillow. Usage:  python3 scripts/bayarea-icons.py
#

from pathlib import Path
from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parent.parent
SOURCE = ROOT / "app-ose/branding/icon.png"
RES = ROOT / "app-ose/src/main/res"
DENSITIES = {"mdpi": 1, "hdpi": 1.5, "xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}


def centered(image: Image.Image, canvas_px: int, width_px: int) -> Image.Image:
    height_px = round(image.height * width_px / image.width)
    canvas = Image.new("RGBA", (canvas_px, canvas_px), (0, 0, 0, 0))
    resized = image.resize((width_px, height_px), Image.LANCZOS)
    canvas.paste(resized, ((canvas_px - width_px) // 2, (canvas_px - height_px) // 2), resized)
    return canvas


logo = Image.open(SOURCE).convert("RGBA")
logo = logo.crop(logo.getbbox())

# white silhouette for themed (monochrome) icons
monochrome = Image.new("RGBA", logo.size, (255, 255, 255, 0))
monochrome.putalpha(logo.getchannel("A"))

for density, scale in DENSITIES.items():
    # adaptive icon layers: 108dp canvas, logo inside the 66dp safe zone
    centered(logo, round(108 * scale), round(62 * scale)).save(RES / f"drawable-{density}/ic_launcher_foreground.png", optimize=True)
    centered(monochrome, round(108 * scale), round(62 * scale)).save(RES / f"drawable-{density}/bayarea_launcher_monochrome.png", optimize=True)

    # legacy icon (< Android 8): 48dp, logo on white rounded square
    size = round(48 * scale)
    legacy = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    ImageDraw.Draw(legacy).rounded_rectangle([0, 0, size - 1, size - 1], radius=round(size * 0.2), fill="white")
    legacy.alpha_composite(centered(logo, size, round(36 * scale)))
    legacy.save(RES / f"mipmap-{density}/ic_launcher.png", optimize=True)

# Google Play icon: 512x512, no transparency (Play applies the rounded mask itself)
play = Image.new("RGBA", (512, 512), "white")
play.alpha_composite(centered(logo, 512, 380))
play.convert("RGB").save(ROOT / "app-ose/branding/play-icon-512.png", optimize=True)
