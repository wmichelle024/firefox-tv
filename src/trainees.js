'use strict';

import { ruleset, rule, dom, type, score, out } from 'fathom-web';
import { euclidean } from 'fathom-web/clusters';
import { ancestors, isVisible, min, page, sigmoid } from 'fathom-web/utilsForFrontend';
import { types, names } from './constants';

const VIEWPORT_DIMENS = { width: 1024, height: 768 };
const VIEWPORT_SIZE = VIEWPORT_DIMENS.width * VIEWPORT_DIMENS.height;
const IMAGE_EXTENSIONS = ['jpg', 'jpeg', 'png', 'gif', 'webp', 'tiff', 'heif', 'indd', 'svg', 'eps', 'pdf'];
const MUSIC_KEYWORDS = ['album', 'song', 'music', 'artist', 'genre'];
const TEXT_NODE = 3;

/* General Page Rules */

// Checks for keyword in page URL
// @param element - document
function hasMusicKeywordInURL() {
    let url = document.URL.toLowerCase();
    return MUSIC_KEYWORDS.some(it => url.includes(it));
}

// Checks for keyword in page title
// @param element - document
function hasMusicKeywordInTitle() {
    let title = document.title.toLowerCase();
    return MUSIC_KEYWORDS.some(it => title.includes(it));
}

// Checks for keyword in search bar placeholder text
// @param element - input element
function hasMusicSearchBar(element) {
    let placeholder = element.placeholder.toLowerCase();
    return MUSIC_KEYWORDS.some(it => placeholder.includes(it));
}

// Return total size of all images to total size (width * height) of text boxes
function totalImageToTextSizeRatio() {
    let allElem = flattenNodes(document.body);
    let artSize = 0;
    let textSize = 0;
    for (let e of allElem) {
        if (e.offsetHeight != null && e.offsetWidth != null && isVisible(e)) {
            if (hasImageAttribute(e)) {
                console.log("Class: " + e.className + " Size: " + e.offsetHeight * e.offsetWidth);
                artSize += e.offsetHeight * e.offsetWidth;
            } else if (elementText(e)) {
                textSize += e.offsetHeight * e.offsetWidth;
            }
        }
    }
    console.log(document.URL);
    console.log(artSize);
    console.log(textSize);

    return artSize / textSize;
}


function elementText(element) {
    if (element.text && element.text.trim().length > 0) return element.text;
    for (let n of element.childNodes) {
        if (n.nodeType == TEXT_NODE && n.textContent.trim().length > 0) {
            return n.textContent;
        }
    }
    return false;
}

function ratioOfSquareToTotalImgsEntryPoint(fnode) {
    const flattened = flattenNodes(fnode.element);
    const normalized = flattened
        .filter(element => hasImageAttribute(element))
        .filter(element => (element.offsetHeight || element.offsetHeight === 0) && (element.offsetWidth || element.offsetWidth === 0))
        .map(element => elementToDimens(element));

    return ratioOfSquareToTotalImgs(normalized);
}

// Return count of square images compared to all images on screen
function ratioOfSquareToTotalImgs(sizes) { // [{height: Number, width: Number}]
    let squareImgs = 0;
    let totalImgs = 0;
    for (let dimen of sizes) {
        if (byAspectRatio(dimen) == 1) {
            squareImgs++
        }
        totalImgs++
    }
    return squareImgs / totalImgs
}

/* Album art Rules */
function byAspectRatio(size) {
    const bigger = Math.max(size.height, size.width);
    const smaller = Math.min(size.height, size.width);

    if (bigger === 0 && smaller === 0) return 1;
    if (bigger === 0 || smaller === 0) return 0;

    // TODO calculations are off
    return smaller / bigger;
};

function divIsImage(element) {
    const id = element.id.toLowerCase();
    const classes = [...element.classList].map(it => it.toLowerCase());

    const allClassifiers = [id, ...classes];

    return allClassifiers.some(it => it.includes("image") || it.includes("img") || it.includes("art") || it.includes("album"));
};

function hasImageAttribute(element) {
    if (element.tagName.toLowerCase() === "canvas") {
        return hasCanvasImgTag(element)
    }
    const attributes = [...element.attributes];
    for (let ext of IMAGE_EXTENSIONS) {
        for (let attr of attributes) {
            if (attr.value.includes(ext)) {
                return true;
            }
        }
    }
    return false;
}

function hasCanvasImgTag(element) {
    let url = element.toDataURL();
    if (url) {
        return IMAGE_EXTENSIONS.some(it => url.includes(it));
    }
    return false;
}

function getHorizontallyCentered(element) {
    const bodyRect = document.body.getBoundingClientRect()
    const elementRect = element.getBoundingClientRect()

    const center = (bodyRect.right - bodyRect.left) / 2;
    const elementCenter = elementRect.x + ((elementRect.right - elementRect.left) / 2);

    const bigger = Math.max(center, elementCenter);
    const smaller = Math.min(center, elementCenter);

    return 1 - ((bigger - smaller) / center);
}

function sizeToViewport(element) {
    return (element.offsetHeight * element.offsetWidth) / VIEWPORT_SIZE;
}

/* Helper functions */
// TODO: move to a different file?

// Return array of all descendent nodes, including root element
// @param element - root element to flatten
function flattenNodes(element) {
    let descendents = [];
    let next = [element];
    while (next.length > 0) {
        let curr = next.shift();
        descendents.push(curr);
        if (curr.children) {
            next = next.concat(Array.from(curr.children));
        }
    }
    return descendents;
}

/**
 * Height and Width may be undefined
 */
function elementToDimens(element) {
    return { height: element.offsetHeight, width: element.offsetWidth }
}

function makeRuleset() {
    const coeffs = [  // [rule name, coefficient]
        [names.HAS_MUSIC_TITLE, 12.008438110351562],
        [names.SQUARE_IMG_RATIO, 3.417603015899658],
        [names.IMG_TEXT_RATIO, 0.46127617359161377]
    ];
    return ruleset(
        [
            rule(dom('body'), type(types.MUSIC)),
            rule(type(types.MUSIC), score(page(totalImageToTextSizeRatio)), { name: names.IMG_TEXT_RATIO }),
            rule(type(types.MUSIC), score(page(ratioOfSquareToTotalImgsEntryPoint)), { name: names.SQUARE_IMG_RATIO }),
            rule(type(types.MUSIC), score(page(hasMusicKeywordInTitle)), { name: names.HAS_MUSIC_TITLE }),
            rule(type(types.MUSIC), out(types.MUSIC))
        ],
        coeffs,
        [[types.MUSIC, -2.9170479774475098]]
    );
};

const config = new Map();

config.set(
    types.MUSIC,
    {
        coeffs: new Map([
            [names.HAS_MUSIC_TITLE, 1],
            [names.SQUARE_IMG_RATIO, 1],
            [names.IMG_TEXT_RATIO, 1]
        ]),
        viewportSize: VIEWPORT_DIMENS,
        vectorType: types.MUSIC,
        rulesetMaker: makeRuleset
    }
);

export default config;
