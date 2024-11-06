package article1be.outfit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "outfit_situation")
@ToString
public class OutfitSituation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outfitSituationSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outfit_seq", nullable = false)
    private Outfit outfit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "situation_seq", nullable = false)
    private Situation situation;

    public OutfitSituation(Outfit outfit, Situation situation) {
        this.outfit = outfit;
        this.situation = situation;
    }
}
