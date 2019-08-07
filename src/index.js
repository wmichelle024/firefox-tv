import {dom, rule, ruleset, type, score, out} from 'fathom-web';
import {types, names} from './constants.js';
import configs from './trainees.js';

const rules = configs.get(types.MUSIC).rulesetMaker();
const target = rules.against(document).get(types.MUSIC)[0];
const title = target.scoreFor(types.MUSIC);

//
//function containsColonsOrDashes(elem) {
//    return true;
//}
//
//// example partially from https://github.com/mozilla/fathom
//var titleFinder = ruleset([
//    // Give any title tag a score of 1, and tag it as title-ish:
//    rule(dom("title"), type('titley')),
//    rule(type("titley"), score(1)),
//    rule(type('titley').max(), out('titley'))
//    ]
//);
//
//var titleNode = titleFinder.against(document).get('titley')[0].element;
//var title = titleNode.innerText;

java.setTitle(title);

console.log("hi");
